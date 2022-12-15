package elevator;

import com.oocourse.TimableOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static elevator.Main.ELEVATOR_DEBUG;
import static java.lang.Math.abs;

public class Elevator implements Runnable {
    private final String id;
    private final int capacity;
    private final int speed;
    private final boolean[] stoppableFloors;
    private final String arrivingMode;
    private final LinkedBlockingQueue<ElevatorCommand> command;
    private final WaitQueues waitQueues;
    private int currentFloor = 1;
    private final LinkedList<Passenger> passengers = new LinkedList<>();

    public Elevator(String id,
                    int capacity,
                    int speed,
                    boolean[] stoppableFloors,
                    WaitQueues waitQueues,
                    LinkedBlockingQueue<ElevatorCommand> command,
                    String arrivingMode) {
        this.id = id;
        this.speed = speed;
        this.capacity = capacity;
        this.stoppableFloors = stoppableFloors;
        this.waitQueues = waitQueues;
        this.command = command;
        this.arrivingMode = arrivingMode;
    }

    private void moveAndArrive() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ignored) {
            // ignore this
        }
        TimableOutput.println("ARRIVE-" + currentFloor + "-" + id);
    }

    private void openTheDoor(boolean passengersGetOff) {
        TimableOutput.println("OPEN-" + currentFloor + "-" + id);
        if (!passengers.isEmpty() && passengersGetOff) {
            passengersGetOff();
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
            // ignore this
        }
        closeTheDoor();
    }

    private void passengersGetOff() {
        for (Passenger p : passengers) {
            if (p.getDestination() == currentFloor || p.hasTransferred(id)) {
                if (p.hasTransferred(id)) {
                    waitQueues.put(currentFloor, p);
                }
                if (ELEVATOR_DEBUG && !System.getProperty("os.name").equals("Linux")) {
                    TimableOutput.println(
                            "OUT-" + p.getId() + "-" + currentFloor + "-" + id
                                    + Arrays.toString(stoppableFloors));
                } else {
                    TimableOutput.println(
                            "OUT-" + p.getId() + "-" + currentFloor + "-" + id);
                }
            }
        }
        passengers.removeIf(p -> p.getDestination() == currentFloor || p.hasTransferred(id));
    }

    private void closeTheDoor() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
            // ignore this
        }
        if (!waitQueues.isEmpty()) {
            passengersGetOn();
        }
        TimableOutput.println("CLOSE-" + currentFloor + "-" + id);
        if (passengers.isEmpty()) {
            startWorking();
        } else {
            moveToFloor(changeTargetFloorByPassengers());
        }
    }

    private void passengersGetOn() {
        for (int i = passengers.size(); i < capacity; i++) {
            Passenger passenger = waitQueues.pollAt(currentFloor);
            if (passenger == null) {
                break;
            }
            if (passenger.hasTransferred(id)) {
                waitQueues.put(currentFloor, passenger);
                continue;
            }
            if (!stoppableFloors[passenger.getDestination()]) {
                passenger.addTransfer(id);
            }
            passengers.add(passenger);
            TimableOutput.println(
                    "IN-" + passenger.getId() + "-" + currentFloor + "-" + id);
        }
    }

    private boolean thereArePassengersToGetOn() {
        return passengers.size() < capacity && waitQueues.thereArePassengersAt(currentFloor);
    }

    private int changeTargetFloorByWaitQueue() {
        return waitQueues.decideOriginFloorFrom(currentFloor, stoppableFloors, arrivingMode);
    }

    private void startWorking() {
        if (waitQueues.isEmpty()) {
            return;
        }
        int targetFloor = changeTargetFloorByWaitQueue();
        if (currentFloor < targetFloor) {
            while (currentFloor < targetFloor) {
                currentFloor++;
                moveAndArrive();
                int floor = changeTargetFloorByWaitQueue();
                if (floor >= targetFloor) {
                    targetFloor = floor;
                }
            }
        } else if (currentFloor > targetFloor) {
            while (currentFloor > targetFloor) {
                currentFloor--;
                moveAndArrive();
                int floor = changeTargetFloorByWaitQueue();
                if (floor <= targetFloor) {
                    targetFloor = floor;
                }
            }
        }
        currentFloor = targetFloor;
        openTheDoor(false);
    }

    private int changeTargetFloorByPassengers() {
        Passenger passenger = passengers.stream().sorted(
                Comparator.comparingInt((Passenger o) -> abs(o.getDestination() - currentFloor))
                        .thenComparingInt(Passenger::getId))
                .collect(Collectors.toList()).get(0);
        int finalFloor = passenger.getDestination();
        int targetFloor = finalFloor;
        if (!stoppableFloors[finalFloor]) {
            if (finalFloor > currentFloor) {
                for (int i = currentFloor; i <= finalFloor; i++) {
                    if (stoppableFloors[i]) {
                        targetFloor = i;
                    }
                }
            } else if (finalFloor < currentFloor) {
                for (int i = currentFloor; i >= finalFloor; i--) {
                    if (stoppableFloors[i]) {
                        targetFloor = i;
                    }
                }
            }
        }
        return targetFloor;
    }

    private void moveToFloor(int floor) {
        if (currentFloor < floor) {
            while (currentFloor < floor) {
                currentFloor++;
                moveAndArrive();
                if (stoppableFloors[currentFloor] && thereArePassengersToGetOn()) {
                    openTheDoor(false);
                    return;
                }
            }
        } else if (currentFloor > floor) {
            while (currentFloor > floor) {
                currentFloor--;
                moveAndArrive();
                if (stoppableFloors[currentFloor] && thereArePassengersToGetOn()) {
                    openTheDoor(false);
                    return;
                }
            }
        }
        openTheDoor(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                switch (command.take()) {
                    case START_WORKING:
                        startWorking();
                        break;
                    case SHUT_DOWN:
                        return;
                    default:
                        break;
                }
            } catch (InterruptedException ignored) {
                // ignore this
            }
        }
    }
}
