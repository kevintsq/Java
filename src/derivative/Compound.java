package derivative;

public abstract class Compound implements Derivable<Compound> {

    @Override
    public abstract Compound takeDerivative();

    @Override
    public abstract String toString();
}
