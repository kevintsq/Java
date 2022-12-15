package bookshelf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class BookShelf implements Cloneable {
    private final HashMap<String, BookSet> bookshelf = new HashMap<>();
    private BigInteger totalQuantity = BigInteger.valueOf(0);

    private static BookSet howToMerge(BookSet oldBook, BookSet newBook) throws RuntimeException {
        if (oldBook == null) {
            try {
                return (BookSet) newBook.clone();
            }
            catch (CloneNotSupportedException ignored) {
                throw new RuntimeException("Oh, no. We fail!");
            }
        } else if (oldBook.equals(newBook)) {
            try {
                BookSet clonedBook = (BookSet) oldBook.clone();
                clonedBook.setQuantity(oldBook.getQuantity() + newBook.getQuantity());
                return clonedBook;
            }
            catch (CloneNotSupportedException ignored) {
                throw new RuntimeException("Oh, no. We fail!");
            }
        } else {
            throw new RuntimeException("Oh, no. We fail!");
        }
    }

    public static BookShelf merge(BookShelf a, BookShelf b) {
        BookShelf c = new BookShelf();
        try {
            for (String book : a.bookshelf.keySet()) {
                c.bookshelf.put(book, (BookSet) a.bookshelf.get(book).clone());
            }
            for (String book : b.bookshelf.keySet()) {
                c.bookshelf.merge(book, b.bookshelf.get(book), BookShelf::howToMerge);
            }
        } catch (CloneNotSupportedException ignored) { ; }
        c.totalQuantity = BigInteger.valueOf(0);
        for (String book : c.bookshelf.keySet()) {
            c.totalQuantity = c.totalQuantity.add(
                    BigInteger.valueOf(c.bookshelf.get(book).getQuantity()));
        }
        return c;
    }

    public double maxPrice() {
        double maxPrice = 0;
        for (String book : bookshelf.keySet()) {
            double price = bookshelf.get(book).getPrice();
            if (price > maxPrice) {
                maxPrice = price;
            }
        }
        return maxPrice;
    }

    public BigDecimal totalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (String book : bookshelf.keySet()) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(bookshelf.get(book).getPrice())
                    .multiply(BigDecimal.valueOf(bookshelf.get(book).getQuantity())));
        }
        return totalPrice;
    }

    public BigInteger getTotalQuantity() throws EmptyShelfException {
        if (totalQuantity.equals(BigInteger.valueOf(0))) {
            throw new EmptyShelfException("Oh, no! This is empty.");
        } else {
            return totalQuantity;
        }
    }

    public void addBook(String[] infoList) throws BookAlreadyExistsException {
        String name = infoList[1];
        if (bookshelf.containsKey(name)) {
            throw new BookAlreadyExistsException("Oh, no! The " + name + " exist.");
        } else {
            String type = infoList[0];
            long quantity = Long.parseLong(infoList[3]);
            totalQuantity = totalQuantity.add(BigInteger.valueOf(quantity));
            switch (type) {
                case "Other":
                    bookshelf.put(name, new BookSet(name,
                            Double.parseDouble(infoList[2]),
                            quantity));
                    break;
                case "OtherA":
                    bookshelf.put(name, new Art(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4])));
                    break;
                case "Novel":
                    bookshelf.put(name, new Novel(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4]),
                            Boolean.parseBoolean(infoList[5])));
                    break;
                case "Poetry":
                    bookshelf.put(name, new Poetry(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4]),
                            infoList[5]));
                    break;
                case "OtherS":
                    bookshelf.put(name, new Science(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4])));
                    break;
                case "Math":
                    bookshelf.put(name, new Math(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4]),
                            Integer.parseInt(infoList[5])));
                    break;
                case "Computer":
                    bookshelf.put(name, new Computer(name,
                            Double.parseDouble(infoList[2]),
                            quantity,
                            Integer.parseInt(infoList[4]),
                            infoList[5]));
                    break;
                default:
                    System.exit(-1);
            }
        }
    }

    public BigInteger removeBook(String name) throws CannotRemoveException {
        if (bookshelf.size() == 0 || !bookshelf.containsKey(name)) {
            throw new CannotRemoveException("mei you wo zhen mei you.");
        } else {
            totalQuantity = totalQuantity.subtract(BigInteger.valueOf(
                    bookshelf.remove(name).getQuantity()));
            return totalQuantity;
        }
    }

    public BookSet findBook(String name) throws BookNotFoundException, EmptyShelfException {
        if (bookshelf.size() == 0) {
            throw new EmptyShelfException("Oh, no! This is empty.");
        } else if (bookshelf.containsKey(name)) {
            return bookshelf.get(name);
        } else {
            throw new BookNotFoundException("Oh, no! We don't have " + name + ".");
        }
    }

    public int getTypes() throws EmptyShelfException {
        if (bookshelf.size() == 0) {
            throw new EmptyShelfException("Oh, no! This is empty.");
        } else {
            HashSet<String> types = new HashSet<>(7);
            for (String book : bookshelf.keySet()) {
                types.add(bookshelf.get(book).getType());
            }
            return types.size();
        }
    }
}
