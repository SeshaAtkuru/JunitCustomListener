package cucumber.examples.java.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gherkin.pickles.Argument;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import cucumber.api.HookTestStep;
import cucumber.api.HookType;
import cucumber.api.PickleStepTestStep;
import cucumber.api.TestCase;
import cucumber.api.event.EmbedEvent;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.SnippetsSuggestedEvent;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.event.TestSourceRead;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.api.event.WriteEvent;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import gherkin.pickles.PickleTag;

public class CustomPlugin implements EventListener {
	private Map<String, Object> currentElementMap;
	private Map<String, Object> currentTestCaseMap;
	private List<Map<String, Object>> currentStepsList;
	private Map<String, Object> currentStepOrHookMap;
	private Map<String, Object> currentBeforeStepHookList = new HashMap<String, Object>();
	private final TestSourcesModel testSources = new TestSourcesModel();
	private String currentFeatureFile;

	public CustomPlugin() {
	}

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		// Event sent when test run in started. TestRunStarted event is sent to
		// handler.
		// TestRunStarted contains the timestamp of the test run start.
		// publisher.registerHandlerFor(TestRunStarted.class, handleRunStarted);

		// Event sent when feature file is read. TestSourceRead event is sent to
		// handler.
		// TestSourceRead contains the location of the feature file and its
		// contents.
		// publisher.registerHandlerFor(TestSourceRead.class, handleSourceRead);

		// Event sent before scenario execution. TestCaseStarted event is sent
		// to handler.
		// TestCaseStarted contains the scenario details like uri, line, steps,
		// tags etc.
		publisher.registerHandlerFor(TestCaseStarted.class, handleCaseStarted);

		// Event sent before step execution. TestStepStarted event is sent to
		// the handler.
		// TestStepStarted contains step details.
		publisher.registerHandlerFor(TestStepStarted.class, handleStepStarted);

		// Event sent after step execution. TestStepFinished event is sent to
		// the handler.
		//// TestStepFinished contains step details and result of the step.
		publisher.registerHandlerFor(TestStepFinished.class, handleStepFinished);

		// Event sent after scenario execution. TestCaseFinished event is sent
		// to handler.
		// TestCaseFinished contains the scenario details and test case result.
		publisher.registerHandlerFor(TestCaseFinished.class, handleCaseFinished);

		// Event sent when test run in finished. TestRunFinished event is sent
		// to handler.
		// TestRunFinished contains the timestamp of the test run end.
		publisher.registerHandlerFor(TestRunFinished.class, handleRunFinished);

		// Event sent when scenario.embed is called inside a hook. EmbedEvent is
		// sent to the handler.
		// publisher.registerHandlerFor(EmbedEvent.class,
		// this::handleEmbedEvent);

		// Event sent when scenario.write is called inside a hook. WriteEvent is
		// sent to the handler.
		// publisher.registerHandlerFor(WriteEvent.class,
		// this::handleWriteEvent);

		// Event sent when step cannot be matched to a step definition.
		// SnippetsSuggestedEvent is sent to handler.
		// publisher.registerHandlerFor(SnippetsSuggestedEvent.class,
		// this::handleSnippetSuggest);

	}

	private EventHandler<TestSourceRead> testSourceReadHandler = new EventHandler<TestSourceRead>() {
		@Override
		public void receive(TestSourceRead event) {
			handleTestSourceRead(event);
		}
	};

	private void handleTestSourceRead(TestSourceRead event) {
		testSources.addTestSourceReadEvent(event.uri, event);
	}

	private void handleTestStepStarted(TestStepStarted event) {
		if (event.testStep instanceof PickleStepTestStep) {
			PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
			if (isFirstStepAfterBackground(testStep)) {
				currentElementMap = currentTestCaseMap;
				currentStepsList = (List<Map<String, Object>>) currentElementMap.get("steps");
			}
			currentStepOrHookMap = createTestStep(testStep);
			// add beforeSteps list to current step
			if (currentBeforeStepHookList.containsKey(HookType.Before.toString())) {
				currentStepOrHookMap.put(HookType.Before.toString(),
						currentBeforeStepHookList.get(HookType.Before.toString()));
				currentBeforeStepHookList.clear();
			}
			currentStepsList.add(currentStepOrHookMap);
		} else if (event.testStep instanceof HookTestStep) {
			HookTestStep hookTestStep = (HookTestStep) event.testStep;
			currentStepOrHookMap = createHookStep(hookTestStep);
			addHookStepToTestCaseMap(currentStepOrHookMap, hookTestStep.getHookType());
		} else {
			throw new IllegalStateException();
		}
	}

	private boolean isFirstStepAfterBackground(PickleStepTestStep testStep) {
		TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
		if (astNode != null) {
			if (currentElementMap != currentTestCaseMap && !TestSourcesModel.isBackgroundStep(astNode)) {
				return true;
			}
		}
		return false;
	}

	private EventHandler<TestCaseStarted> handleCaseStarted = new EventHandler<TestCaseStarted>() {
		@Override
		public void receive(TestCaseStarted event) {
			System.out.println("TestCaseStarted....................");
			currentTestCaseMap = createTestCase(event.testCase);
		}
	};

	private Map<String, Object> createTestCase(TestCase testCase) {
		Map<String, Object> testCaseMap = new HashMap<String, Object>();
		testCaseMap.put("name", testCase.getName());
		testCaseMap.put("line", testCase.getLine());
		testCaseMap.put("type", "scenario");
		TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testCase.getLine());
		if (astNode != null) {
			testCaseMap.put("id", TestSourcesModel.calculateId(astNode));
			ScenarioDefinition scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
			testCaseMap.put("keyword", scenarioDefinition.getKeyword());
			testCaseMap.put("description",
					scenarioDefinition.getDescription() != null ? scenarioDefinition.getDescription() : "");
		}
		testCaseMap.put("steps", new ArrayList<Map<String, Object>>());
		if (!testCase.getTags().isEmpty()) {
			List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
			for (PickleTag tag : testCase.getTags()) {
				Map<String, Object> tagMap = new HashMap<String, Object>();
				tagMap.put("name", tag.getName());
				tagList.add(tagMap);
			}
			testCaseMap.put("tags", tagList);
		}
		return testCaseMap;
	}

	private Map<String, Object> createTestStep(PickleStepTestStep testStep) {
		Map<String, Object> stepMap = new HashMap<String, Object>();
		stepMap.put("name", testStep.getStepText());
		stepMap.put("line", testStep.getStepLine());
		TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile, testStep.getStepLine());
		if (!testStep.getStepArgument().isEmpty()) {
			Argument argument = testStep.getStepArgument().get(0);
			if (argument instanceof PickleString) {
				stepMap.put("doc_string", createDocStringMap(argument));
			} else if (argument instanceof PickleTable) {
				stepMap.put("rows", createDataTableList(argument));
			}
		}
		if (astNode != null) {
			Step step = (Step) astNode.node;
			stepMap.put("keyword", step.getKeyword());
		}

		return stepMap;
	}

	private Map<String, Object> createHookStep(HookTestStep hookTestStep) {
		return new HashMap<String, Object>();
	}

	private void addHookStepToTestCaseMap(Map<String, Object> currentStepOrHookMap, HookType hookType) {
		String hookName;
		if (hookType.toString().contains("after"))
			hookName = "after";
		else
			hookName = "before";

		Map<String, Object> mapToAddTo;
		switch (hookType) {
		case Before:
			mapToAddTo = currentTestCaseMap;
			break;
		case After:
			mapToAddTo = currentTestCaseMap;
			break;
		case BeforeStep:
			mapToAddTo = currentBeforeStepHookList;
			break;
		case AfterStep:
			mapToAddTo = currentStepsList.get(currentStepsList.size() - 1);
			break;
		default:
			mapToAddTo = currentTestCaseMap;
		}

		if (!mapToAddTo.containsKey(hookName)) {
			mapToAddTo.put(hookName, new ArrayList<Map<String, Object>>());
		}
		((List<Map<String, Object>>) mapToAddTo.get(hookName)).add(currentStepOrHookMap);
	}

	private Map<String, Object> createDocStringMap(Argument argument) {
		Map<String, Object> docStringMap = new HashMap<String, Object>();
		PickleString docString = ((PickleString) argument);
		docStringMap.put("value", docString.getContent());
		docStringMap.put("line", docString.getLocation().getLine());
		docStringMap.put("content_type", docString.getContentType());
		return docStringMap;
	}

	private List<Map<String, Object>> createDataTableList(Argument argument) {
		List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
		for (PickleRow row : ((PickleTable) argument).getRows()) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("cells", createCellList(row));
			rowList.add(rowMap);
		}
		return rowList;
	}

	private List<String> createCellList(PickleRow row) {
		List<String> cells = new ArrayList<String>();
		for (PickleCell cell : row.getCells()) {
			cells.add(cell.getValue());
		}
		return cells;
	}

	private EventHandler<TestStepFinished> handleStepFinished = new EventHandler<TestStepFinished>() {
		@Override
		public void receive(TestStepFinished event) {
			System.out.println("TestStepFinished..................");
		}
	};
	private EventHandler<TestStepStarted> handleStepStarted = new EventHandler<TestStepStarted>() {

		@Override
		public void receive(TestStepStarted event) {
			if (currentFeatureFile == null || !currentFeatureFile.equals(event.getTestCase().getUri())) {
				currentFeatureFile = event.getTestCase().getUri();

			}
			handleTestStepStarted(event);
			// System.out.println("TestStepStarted.................."+event.testStep.getCodeLocation());

		}
	};
	private EventHandler<TestCaseFinished> handleCaseFinished = new EventHandler<TestCaseFinished>() {
		@Override
		public void receive(TestCaseFinished event) {
			System.out.println();
		}
	};
	private EventHandler<TestRunFinished> handleRunFinished = new EventHandler<TestRunFinished>() {
		@Override
		public void receive(TestRunFinished event) {
			System.out.println();
		}
	};

}