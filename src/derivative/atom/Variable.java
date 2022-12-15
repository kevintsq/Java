package derivative.atom;

import derivative.Atom;

import java.math.BigInteger;

public class Variable extends Atom {
    @Override
    public Constant takeDerivative() {
        return new Constant(BigInteger.ONE);
    }

    @Override
    public String toString() {
        return "x";
    }
}
