package bookshelf;

import java.util.Objects;

public class Computer extends Science {
    private String major;

    public Computer(String name, double price, long quantity, int year, String major) {
        super(name, price, quantity, year);
        this.major = major;
    }

    @Override
    public String toString() {
        return super.toString() + " " + major;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Computer computer = (Computer) o;
        return major.equals(computer.major);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), major);
    }

    public String getType() {
        return "Computer";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Computer computer = (Computer) super.clone();
        computer.major = getMajor();
        return computer;
    }
}
