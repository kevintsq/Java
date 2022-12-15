package elevator;

import java.util.HashSet;
import java.util.Objects;

public class Passenger implements Comparable<Passenger> {
    private final int id;
    private final int origin;
    private final int destination;
    private final HashSet<String> transferList = new HashSet<>();

    public Passenger(int id, int origin, int destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
    }

    public void addTransfer(String elevatorId) {
        transferList.add(elevatorId);
    }

    public boolean hasTransferred(String elevatorId) {
        return transferList.contains(elevatorId);
    }

    public int getId() {
        return id;
    }

    public int getOrigin() {
        return origin;
    }

    public int getDestination() {
        return destination;
    }

    @Override
    public int compareTo(Passenger o) {
        int flag = o.origin - origin;
        if (flag == 0) {
            flag = o.destination - destination;
        }
        if (flag == 0) {
            flag = o.id - id;
        }
        return flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Passenger passenger = (Passenger) o;
        return id == passenger.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
