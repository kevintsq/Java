package elevator;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static elevator.Main.HIGHEST_FLOOR;
import static elevator.Main.LOWEST_FLOOR;
import static elevator.Main.MAX_CMD_CNT;

public class Scheduler implements Runnable {
    private final WaitQueues waitQueues;
    private final HashMap<String, LinkedBlockingQueue<ElevatorCommand>> elevatorCommands;
    private final String arrivingMode;
    private final ElevatorInput requestInput;
    static final boolean[] ELEVATOR_A_STOPPABLE_FLOORS = new boolean[HIGHEST_FLOOR + LOWEST_FLOOR];
    static final boolean[] ELEVATOR_B_STOPPABLE_FLOORS = new boolean[HIGHEST_FLOOR + LOWEST_FLOOR];
    static final boolean[] ELEVATOR_C_STOPPABLE_FLOORS = new boolean[HIGHEST_FLOOR + LOWEST_FLOOR];

    public Scheduler(WaitQueues waitQueues,
                     HashMap<String, LinkedBlockingQueue<ElevatorCommand>> elevatorCommands,
                     ElevatorInput requestInput,
                     String arrivingMode) {
        this.waitQueues = waitQueues;
        this.elevatorCommands = elevatorCommands;
        this.requestInput = requestInput;
        this.arrivingMode = arrivingMode;
        for (int i = LOWEST_FLOOR; i <= HIGHEST_FLOOR; i++) {
            ELEVATOR_A_STOPPABLE_FLOORS[i] = true;
            if ((i & 1) == 1) {
                ELEVATOR_B_STOPPABLE_FLOORS[i] = true;
            }
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 18:
                case 19:
                case 20:
                    ELEVATOR_C_STOPPABLE_FLOORS[i] = true;
                    break;
                default:
            }
        }
    }

    private void enableElevator(String id, String type) {
        LinkedBlockingQueue<ElevatorCommand> elevatorCommand =
                new LinkedBlockingQueue<>(MAX_CMD_CNT);
        elevatorCommands.put(id, elevatorCommand);
        switch (type) {
            case "A":
                new Thread(new Elevator(id,
                        8,
                        600,
                        ELEVATOR_A_STOPPABLE_FLOORS,
                        waitQueues,
                        elevatorCommand,
                        arrivingMode), "Elevator-" + id).start();
                break;
            case "B":
                new Thread(new Elevator(id,
                        6,
                        400,
                        ELEVATOR_B_STOPPABLE_FLOORS,
                        waitQueues,
                        elevatorCommand,
                        arrivingMode), "Elevator-" + id).start();
                break;
            case "C":
                new Thread(new Elevator(id,
                        4,
                        200,
                        ELEVATOR_C_STOPPABLE_FLOORS,
                        waitQueues,
                        elevatorCommand,
                        arrivingMode), "Elevator-" + id).start();
                break;
            default:
        }
    }

    @Override
    public void run() {
        enableElevator("3", "C");
        enableElevator("2", "B");
        enableElevator("1", "A");
        while (true) {
            Request request = requestInput.nextRequest();
            if (request == null) {
                // when request == null
                // it means there are no more lines in stdin
                try {
                    requestInput.close();
                    for (LinkedBlockingQueue<ElevatorCommand> elevatorCommand :
                            elevatorCommands.values()) {
                        elevatorCommand.put(ElevatorCommand.SHUT_DOWN);
                    }
                } catch (Exception ignored) {
                    // ignore this
                }
                return;
            } else if (request instanceof PersonRequest) {
                PersonRequest p = (PersonRequest) request;
                waitQueues.put(p.getFromFloor(), new Passenger(
                        p.getPersonId(), p.getFromFloor(), p.getToFloor()));
                try {
                    for (LinkedBlockingQueue<ElevatorCommand> elevatorCommand :
                            elevatorCommands.values()) {
                        elevatorCommand.put(ElevatorCommand.START_WORKING);
                    }
                } catch (InterruptedException ignored) {
                    // ignore this
                }
            } else if (request instanceof ElevatorRequest) {
                ElevatorRequest e = (ElevatorRequest) request;
                enableElevator(e.getElevatorId(), e.getElevatorType());
            }
        }
    }
}
