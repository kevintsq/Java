package social;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("einf-" + count + ", " + id + "-" + RECORD.get(id));
    }

}
