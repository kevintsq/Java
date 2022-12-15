package derivative.compound;

import derivative.Compound;

public class Cosine extends Compound {
    private final Compound value;

    public Cosine(Compound value) {
        this.value = value;
    }

    @Override
    public Compound takeDerivative() {
        return new Multiply(value.takeDerivative(), new Negate(new Sine(value)));
    }

    @Override
    public String toString() {
        if (value instanceof Multiply || value instanceof Add || value instanceof Negate) {
            return "cos((" + value + "))";
        }
        return "cos(" + value + ")";
    }
}
