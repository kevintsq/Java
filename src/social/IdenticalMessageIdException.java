package social;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class IdenticalMessageIdException extends EqualMessageIdException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public IdenticalMessageIdException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("emi-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
