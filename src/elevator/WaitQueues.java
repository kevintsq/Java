package elevator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import static elevator.Main.HIGHEST_FLOOR;
import static elevator.Main.LOWEST_FLOOR;
import static elevator.Main.MAX_CMD_CNT;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class WaitQueues {

    private final ArrayList<PriorityBlockingQueue<Passenger>> waitQueues =
            new ArrayList<>(HIGHEST_FLOOR + LOWEST_FLOOR);

    public WaitQueues() {
        for (int i = 0; i < LOWEST_FLOOR; i++) {
            waitQueues.add(i, null);
        }
        for (int i = LOWEST_FLOOR; i <= HIGHEST_FLOOR; i++) {
            int floor = i;
            waitQueues.add(i, new PriorityBlockingQueue<>(MAX_CMD_CNT,
                    Comparator.comparingInt((Passenger o) -> abs(o.getDestination() - floor))
                            .thenComparingInt(Passenger::getId)));
        }
    }

    public boolean isEmpty() {
        for (int i = LOWEST_FLOOR; i <= HIGHEST_FLOOR; i++) {
            PriorityBlockingQueue<Passenger> waitQueue = waitQueues.get(i);
            if (!waitQueue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void put(int floor, Passenger passenger) {
        waitQueues.get(floor).put(passenger);
    }

    public Passenger pollAt(int floor) {
        return waitQueues.get(floor).poll();
    }

    public boolean thereArePassengersAt(int floor) {
        return !waitQueues.get(floor).isEmpty();
    }

    public int decideOriginFloorFrom(int floor, boolean[] stoppableFloors, String arrivingMode) {
        int distance;
        boolean isNightMode = arrivingMode.equals("Night");
        if (isNightMode) {
            distance = 0;
        } else {
            distance = max((HIGHEST_FLOOR - floor), (floor - LOWEST_FLOOR));
        }
        boolean upward = true;
        boolean noMove = true;
        for (int i = LOWEST_FLOOR; i <= HIGHEST_FLOOR; i++) {
            if (!stoppableFloors[i]) {
                continue;
            }
            PriorityBlockingQueue<Passenger> waitQueue = waitQueues.get(i);
            if (!waitQueue.isEmpty()) {
                Passenger passenger = waitQueue.peek();
                if (passenger != null && stoppableFloors[passenger.getDestination()]) {
                    noMove = false;
                    int tmp = i - floor;
                    int tmpAbs = abs(tmp);
                    if (isNightMode && tmpAbs > distance || !isNightMode && tmpAbs <= distance) {
                        distance = tmpAbs;
                        upward = tmp == tmpAbs;
                    }
                }
            }
        }
        if (noMove) {
            return floor;
        } else if (upward) {
            return floor + distance;
        } else {
            return floor - distance;
        }
    }
}
