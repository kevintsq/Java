package bookshelf;

import java.util.Objects;
import java.util.StringJoiner;

public class BookSet implements Cloneable {
    private String name;
    private double price;
    private long quantity;

    public BookSet(String name, double price, long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return "Other";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookSet bookSet = (BookSet) o;
        return name.equals(bookSet.name) &&
                Double.compare(bookSet.price, price) == 0 &&
                getType().equals(bookSet.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add(name)
                .add(String.valueOf(price))
                .add(String.valueOf(quantity))
                .toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BookSet bookSet = (BookSet) super.clone();
        bookSet.name = getName();
        bookSet.price = price;
        bookSet.quantity = quantity;
        return bookSet;
    }
}
