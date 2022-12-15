package derivative.compound;

import derivative.Compound;

public class Multiply extends Compound {
    private final Compound leftValue;
    private final Compound rightValue;

    public Multiply(Compound leftValue, Compound rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public Compound takeDerivative() {
        return new Add(new Multiply(leftValue.takeDerivative(), rightValue),
                       new Multiply(leftValue, rightValue.takeDerivative()));
    }

    @Override
    public String toString() {
        String leftValue = this.leftValue.toString();
        String rightValue = this.rightValue.toString();
        if (leftValue.equals("0") || rightValue.equals("0")) {
            return "0";
        } else if (leftValue.equals("1")) {
            return rightValue;
        } else if (rightValue.equals("1")) {
            return leftValue;
        } else if (rightValue.startsWith("-")) {
            return leftValue + "*(" + rightValue + ")";
        }
        return leftValue + "*" + rightValue;
    }
}
