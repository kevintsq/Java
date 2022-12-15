package bookshelf;

public class Art extends BookSet {
    private int age;

    public Art(String name, double price, long quantity, int age) {
        super(name, price, quantity);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return "Art";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Art art = (Art) o;
        return age == art.age;
    }

    @Override
    public String toString() {
        return super.toString() + " " + age;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Art art = (Art) super.clone();
        art.age = age;
        return art;
    }
}
