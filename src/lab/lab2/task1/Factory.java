package lab.lab2.task1;

import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        if ("Car".equals(type)) {
            return new Car(Integer.parseInt(ops.get(2)),
                    Integer.parseInt(ops.get(3)),
                    Integer.parseInt(ops.get(4)));
        } else if ("Sprinkler".equals(type)) {
            return new Sprinkler(Integer.parseInt(ops.get(2)),
                    Integer.parseInt(ops.get(3)),
                    Integer.parseInt(ops.get(4)));
        } else {
            return new Bus(Integer.parseInt(ops.get(2)),
                    Integer.parseInt(ops.get(3)),
                    Integer.parseInt(ops.get(4)));
        }
    }
}
