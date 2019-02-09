package cucumber.examples.java.calculator;

import org.junit.Assert;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;

public class BasicCalculatorStepDefs implements En{
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

}
