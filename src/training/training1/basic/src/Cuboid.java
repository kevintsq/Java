package training.training1.basic.src;

public class Cuboid implements Calculate {
    private double length, width, height;

    public Cuboid(int length) {
        this.length = length;
    }

    public Cuboid(int l, int w, int h) {
        this.length = l;
        this.height = h;
        this.width = w;
    }

    public double getLength() {
        return length;
    }

    public double getVolume() {
        return length * width * height;
    }
}
