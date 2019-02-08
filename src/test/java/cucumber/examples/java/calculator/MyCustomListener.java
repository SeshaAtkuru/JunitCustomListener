package cucumber.examples.java.calculator;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class MyCustomListener extends RunListener {

    @Override
    public void testStarted(Description description){
        System.out.println("Test Started: "+description.getDisplayName());
    }

    @Override
    public void testFinished(Description description){
//        System.out.println("Test Finished: "+description.getDisplayName());
    }
}
