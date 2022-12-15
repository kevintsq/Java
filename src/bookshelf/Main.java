package bookshelf;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int bookShelfNum = scanner.nextInt();
        BookShelf[] bookShelves = new BookShelf[2000];
        for (int i = 0; i <= bookShelfNum; i++) {
            bookShelves[i] = new BookShelf();
        }
        int operations = scanner.nextInt();
        for (int i = 0; i < operations; i++) {
            int cases = scanner.nextInt();
            int index = scanner.nextInt();
            String[] info = scanner.nextLine().trim().split("\\s+");
            switch (cases) {
                case 1:
                    try { System.out.println(bookShelves[index].findBook(info[0])); }
                    catch (BookNotFoundException | EmptyShelfException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try { System.out.println(bookShelves[index].getTypes()); }
                    catch (EmptyShelfException e) { System.out.println(e.getMessage()); }
                    break;
                case 3:
                    try { System.out.println(bookShelves[index].getTotalQuantity()); }
                    catch (EmptyShelfException e) { System.out.println(e.getMessage()); }
                    break;
                case 4:
                    try { bookShelves[index].addBook(info); }
                    catch (BookAlreadyExistsException e) { System.out.println(e.getMessage()); }
                    break;
                case 5:
                    try { System.out.println(bookShelves[index].removeBook(info[0])); }
                    catch (CannotRemoveException e) { System.out.println(e.getMessage()); }
                    break;
                case 6:
                    try {
                        bookShelves[bookShelfNum + 1] =
                                BookShelf.merge(bookShelves[index],
                                        bookShelves[Integer.parseInt(info[0])]);
                        System.out.println(++bookShelfNum);
                    } catch (RuntimeException e) { System.out.println(e.getMessage()); }
                    break;
                default:
                    System.exit(-1);
            }
        }
    }
}
