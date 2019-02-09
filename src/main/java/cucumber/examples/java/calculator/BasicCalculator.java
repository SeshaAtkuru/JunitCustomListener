package cucumber.examples.java.calculator;

public class BasicCalculator {
	int num1, num2, sum;

	public int getNum1() {
		return num1;
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int add() {
		return num1 + num2;
	}

	public int subtract() {
		return num1 * num2;
	}
	
	public int divide() {
		return num1 / num2;
	}
	
}
