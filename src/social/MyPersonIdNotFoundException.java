package social;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("pinf-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
