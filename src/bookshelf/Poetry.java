package bookshelf;

public class Poetry extends Art {
    private String author;

    public Poetry(String name, double price, long quantity, int age, String author) {
        super(name, price, quantity, age);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return "Poetry";
    }

    @Override
    public String toString() {
        return super.toString() + " " + author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        Poetry poetry = (Poetry) o;
        return author.equals(poetry.author);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Poetry poetry = (Poetry) super.clone();
        poetry.author = getAuthor();
        return poetry;
    }
}
