package social;

import com.oocourse.spec3.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class IdenticalGroupIdException extends EqualGroupIdException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public IdenticalGroupIdException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("egi-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
