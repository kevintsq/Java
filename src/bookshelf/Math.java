package bookshelf;

public class Math extends Science {
    private int grade;

    public Math(String name, double price, long quantity, int year, int grade) {
        super(name, price, quantity, year);
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getType() {
        return "Math";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Math math = (Math) o;
        return grade == math.grade;
    }

    @Override
    public String toString() {
        return super.toString() + " " + grade;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Math math = (Math) super.clone();
        math.grade = grade;
        return math;
    }
}
