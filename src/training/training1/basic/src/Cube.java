package training.training1.basic.src;

public class Cube extends Cuboid {

    public Cube(int a) {
        super(a);
    }

    public double getVolume() {
        return Math.pow(super.getLength(), 3);
    }
}
