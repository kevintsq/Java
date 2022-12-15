package social;

import com.oocourse.spec3.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class IdenticalPersonIdException extends EqualPersonIdException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public IdenticalPersonIdException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("epi-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
