package cucumber.examples.java.calculator;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import cucumber.api.event.EventHandler;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;

public class MyCustomListener extends RunListener{

    @Override
    public void testStarted(Description description){
        System.out.println("Test Started: "+description.getDisplayName());
    }

    @Override
    public void testFinished(Description description){
//        System.out.println("Test Finished: "+description.getDisplayName());
    }

  
}
