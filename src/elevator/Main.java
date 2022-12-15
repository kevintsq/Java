package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    static final boolean DEBUG = true;  // TODO: change before push!!
    static final boolean ELEVATOR_DEBUG = false;  // TODO: change before push!!
    static final int MAX_CMD_CNT = 50;
    static final int HIGHEST_FLOOR = 20;
    static final int LOWEST_FLOOR = 1;

    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        // Be sure to initialize the start timestamp at the very beginning.
        final WaitQueues waitQueues = new WaitQueues();
        final HashMap<String, LinkedBlockingQueue<ElevatorCommand>>
                elevatorCommands = new HashMap<>();

        PipedOutputStream myPipeOut = new PipedOutputStream();
        PipedInputStream myPipeIn = new PipedInputStream();
        try {
            myPipeOut.connect(myPipeIn);
        } catch (IOException e) {
            System.exit(-1);
        }
        ElevatorInput requestInput;
        if (DEBUG && !System.getProperty("os.name").equals("Linux")) {
            requestInput = new ElevatorInput(myPipeIn);
            new Thread(new DebugInput(myPipeOut), "DebugInput").start();
        } else {
            requestInput = new ElevatorInput(System.in);
        }
        String arrivingMode = requestInput.getArrivingPattern();

        new Thread(new Scheduler(waitQueues, elevatorCommands,
                requestInput, arrivingMode), "Scheduler").start();
    }
}
