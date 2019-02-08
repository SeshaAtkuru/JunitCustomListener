package cucumber.examples.java.calculator;

import cucumber.api.junit.Cucumber;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.IOException;

public class MyCustomRunner extends Cucumber {
    public MyCustomRunner(Class clazz) throws InitializationError{
        super(clazz);
    }
    @Override
    public void run(RunNotifier runNotifier){
        runNotifier.addListener(new MyCustomListener());
        super.run(runNotifier);
    }
}
