package social;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("minf-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
