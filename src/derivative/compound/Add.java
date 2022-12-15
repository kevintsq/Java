package derivative.compound;

import derivative.Compound;

public class Add extends Compound {
    private final Compound leftValue;
    private final Compound rightValue;

    public Add(Compound leftValue, Compound rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public Compound takeDerivative() {
        return new Add(leftValue.takeDerivative(), rightValue.takeDerivative());
    }

    @Override
    public String toString() {
        String leftValue = this.leftValue.toString();
        String rightValue = this.rightValue.toString();
        if (leftValue.equals("0")) {
            return rightValue;
        } else if (rightValue.equals("0")) {
            return leftValue;
        } else if (rightValue.startsWith("-")) {
            return "(" + leftValue + rightValue + ")";
        }
        return "(" + leftValue + "+" + rightValue + ")";
    }
}
