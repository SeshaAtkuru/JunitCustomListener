package cucumber.examples.java.calculator;

import cucumber.api.CucumberOptions;
//import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

//@RunWith(Cucumber.class)
@RunWith(MyCustomRunner.class)
@CucumberOptions(plugin = "json:target/cucumber-report.json",features = {"src/test/resources"}, junit = {"--step-notifications"})
public class RunCukesuJunitTest {

}