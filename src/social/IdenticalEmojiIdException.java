package social;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class IdenticalEmojiIdException extends EqualEmojiIdException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public IdenticalEmojiIdException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("eei-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
