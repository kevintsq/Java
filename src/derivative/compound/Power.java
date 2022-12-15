package derivative.compound;

import derivative.Compound;
import derivative.atom.Constant;

import java.math.BigInteger;

public class Power extends Compound {
    private final Constant power;
    private final Compound base;

    public Power(Compound base, Constant power) {
        this.power = power;
        this.base = base;
    }

    @Override
    public Compound takeDerivative() {
        return new Multiply(base.takeDerivative(),
                            new Multiply(power,
                                         new Power(base,
                                                 power.add(new Constant(BigInteger.valueOf(-1))))));
    }

    @Override
    public String toString() {
        String power = this.power.toString();
        if (power.equals("0")) {
            return "1";
        } else if (power.equals("1")) {
            return base.toString();
        }
        return base + "**" + power;
    }
}
