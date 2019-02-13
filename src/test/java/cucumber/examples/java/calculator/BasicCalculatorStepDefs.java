package cucumber.examples.java.calculator;

import org.junit.Assert;

import cucumber.api.Scenario;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;

public class BasicCalculatorStepDefs implements En, EventListener{
	int sum = 0, num1 =0, num2=0;
	String op;

	public BasicCalculatorStepDefs() {

		Given("Open Caluclator", () -> {
			// Write code here that turns the phrase above into concrete actions
			System.out.println("Calculator is open powered up ready to use ...");
			
		});

		When("enter {int} and {int}", (Integer int1, Integer int2) -> {
			// Write code here that turns the phrase above into concrete actions
			num1 = int1.intValue(); num2 = int2.intValue();
		});

		When("press plus", () -> {
			// Write code here that turns the phrase above into concrete actions
			sum = num1 + num2;
		});

		Then("calculator displays {int}", (Integer int1) -> {
			// Write code here that turns the phrase above into concrete actions
			Assert.assertEquals(sum, int1.intValue());
		});

		When("press minus", () -> {
			// Write code here that turns the phrase above into concrete actions
			sum = num1 - num2;
		});

		When("press multiply", () -> {
			// Write code here that turns the phrase above into concrete actions
			sum = num1 * num2;
		});

		When("press divide", () -> {
			// Write code here that turns the phrase above into concrete actions
			sum = num1 / num2;
		});

	}

	@cucumber.api.java.BeforeStep
	public void beforeStep(Scenario scenario){
		System.out.println("before step :"+scenario.getName());
	}
	  private EventHandler<TestCaseStarted> caseStartedHandler= new EventHandler<TestCaseStarted>() {
	        @Override
	        public void receive(TestCaseStarted event) {
	        	System.out.println("TestCaseStarted....................");
	        }
	    };
	    private EventHandler<TestStepFinished> stepFinishedHandler = new EventHandler<TestStepFinished>() {
	        @Override
	        public void receive(TestStepFinished event) {
	        	System.out.println("TestStepFinished..................");
	        }
	    };
	    private EventHandler<TestStepStarted> stepStartedHandler = new EventHandler<TestStepStarted>() {

			@Override
			public void receive(TestStepStarted event) {
				System.out.println("TestStepStarted..................");
				
			}
	    };
	    private EventHandler<TestCaseFinished> caseFinishedHandler = new EventHandler<TestCaseFinished>() {
	        @Override
	        public void receive(TestCaseFinished event) {
	        	System.out.println();
	        }
	    };
	    private EventHandler<TestRunFinished> runFinishedHandler = new EventHandler<TestRunFinished>() {
	        @Override
	        public void receive(TestRunFinished event) {
	            System.out.println();
	        }
	    };


	    @Override
	    public void setEventPublisher(EventPublisher publisher) {
	        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
	        publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
	        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
	        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
	    }

}
