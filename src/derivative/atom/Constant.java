package derivative.atom;

import derivative.Atom;

import java.math.BigInteger;

public class Constant extends Atom {
    private final BigInteger value;

    public Constant(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    public Constant add(Constant constant) {
        return new Constant(value.add(constant.value));
    }

    public Constant multiply(Constant constant) {
        return new Constant(value.multiply(constant.value));
    }

    @Override
    public Constant takeDerivative() {
        return new Constant(BigInteger.ZERO);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
