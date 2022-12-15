package derivative.compound;

import derivative.Compound;

public class Sine extends Compound {
    private final Compound value;

    public Sine(Compound value) {
        this.value = value;
    }

    @Override
    public Compound takeDerivative() {
        return new Multiply(value.takeDerivative(), new Cosine(value));
    }

    @Override
    public String toString() {
        if (value instanceof Multiply || value instanceof Add || value instanceof Negate) {
            return "sin((" + value + "))";
        }
        return "sin(" + value + ")";
    }
}
