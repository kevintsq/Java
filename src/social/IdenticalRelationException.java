package social;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class IdenticalRelationException extends EqualRelationException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> RECORD = new HashMap<>();
    private final int id1;
    private final int id2;

    public IdenticalRelationException(int id1, int id2) {
        if (id1 > id2) {
            this.id2 = id1;
            this.id1 = id2;
            RECORD.merge(id2, 1, Integer::sum);
        } else {
            this.id1 = id1;
            this.id2 = id2;
            if (id1 < id2) {
                RECORD.merge(id2, 1, Integer::sum);
            }
        }
        count++;
        RECORD.merge(id1, 1, Integer::sum);
    }

    @Override
    public void print() {
        System.out.println("er-" + count + ", " + id1 + "-" +
                RECORD.get(id1) + ", " + id2 + "-" + RECORD.get(id2));
    }
}
