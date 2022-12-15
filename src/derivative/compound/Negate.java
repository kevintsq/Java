package derivative.compound;

import derivative.Compound;
import derivative.atom.Variable;

public class Negate extends Compound {
    private final Compound value;

    public Negate(Compound value) {
        this.value = value;
    }

    @Override
    public Compound takeDerivative() {
        return new Negate(value.takeDerivative());
    }

    @Override
    public String toString() {
        String value = this.value.toString();
        if (value.equals("0")) {
            return "0";
        } else if (value.startsWith("-")) {
            return value.substring(1);
        } else if (this.value instanceof Sine
                || this.value instanceof Cosine
                || this.value instanceof Variable) {
            return "-1*" + value;
        }
        return "-" + value;
    }
}
