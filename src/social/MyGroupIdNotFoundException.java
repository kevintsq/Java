package social;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        count++;
        RECORD.merge(id, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("ginf-" + count + ", " + id + "-" + RECORD.get(id));
    }
}
