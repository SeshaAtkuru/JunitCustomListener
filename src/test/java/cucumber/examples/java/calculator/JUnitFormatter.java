package cucumber.examples.java.calculator;


import org.w3c.dom.Element;

import cucumber.api.event.EventHandler;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;

final class JUnitFormatter implements EventListener{

    JUnitFormatter(){
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

