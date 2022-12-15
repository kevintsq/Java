package bookshelf;

public class Science extends BookSet {
    private int year;

    public Science(String name, double price, long quantity, int year) {
        super(name, price, quantity);
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return "Science";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Science science = (Science) o;
        return year == science.year;
    }

    @Override
    public String toString() {
        return super.toString() + " " + year;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Science science = (Science) super.clone();
        science.year = year;
        return science;
    }
}
