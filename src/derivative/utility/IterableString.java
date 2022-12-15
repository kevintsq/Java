package derivative.utility;

import java.util.Iterator;

public class IterableString implements Iterator<Character>, Iterable<Character> {
    private final String value;
    private int cursor = 0;

    public IterableString(String value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return cursor < value.length();
    }

    @Override
    public Character next() {
        if (!hasNext()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        return value.charAt(cursor++);
    }

    public char previous() {
        return value.charAt(--cursor);
    }

    public String skipCharsBy(int count) {
        cursor += count;
        return remaining();
    }

    public char current() {
        if (hasNext()) {
            return value.charAt(cursor);
        } else {
            return '\0';
        }
    }

    public String remaining() {
        return value.substring(cursor);
    }

    @Override
    public String toString() {
        return remaining();
    }

    public boolean startsWith(String s) {
        return remaining().startsWith(s);
    }

    @Override
    public Iterator<Character> iterator() {
        return this;
    }
}
