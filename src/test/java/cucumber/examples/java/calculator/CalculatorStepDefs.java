package cucumber.examples.java.calculator;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;

public class CalculatorStepDefs {
    @Given("^Open Calculator$")
    public void open_calculator()  {
        System.out.println("In Given ");
    }

    @When("^Typed 10 plus 5$")
    public void typed_10_plus_5()  {
        System.out.println("In When ");
    }

    @Then("^Calculator displays 15$")
    public void calculator_displays_15()  {
        System.out.println("In Then ");
    }

    @And("^Calculator powered up$")
    public void calculator_powered_up()  {
        System.out.println("In And ");
    }
}
