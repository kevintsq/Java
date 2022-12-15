package umlparser;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlStateMachine {
    private final UmlStateMachine stateMachine;
    private final HashMap<String, UmlStateLikeObject> states = new HashMap<>();

    public MyUmlStateMachine(UmlStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public UmlStateMachine getStateMachine() {
        return stateMachine;
    }

    public void addState(UmlStateLikeObject state) {
        states.put(state.getState().getId(), state);
    }

    public int getStateCount() {
        return states.size();
    }

    private UmlStateLikeObject testAndGetState(String stateName)
            throws StateDuplicatedException, StateNotFoundException {
        int count = 0;
        UmlStateLikeObject returnState = null;
        for (UmlStateLikeObject state : states.values()) {
            if (state instanceof MyUmlState && state.getState().getName().equals(stateName)) {
                if (++count > 1) {
                    throw new StateDuplicatedException(stateMachine.getName(), stateName);
                }
                returnState = state;
            }
        }
        if (returnState == null) {
            throw new StateNotFoundException(stateMachine.getName(), stateName);
        }
        return returnState;
    }

    public int getSubsequentStateCount(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        return testAndGetState(stateName).getNextStateCount();
    }

    public List<String> getTransitionTriggerNames(String stateName1, String stateName2)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        UmlStateLikeObject sourceState = testAndGetState(stateName1);
        UmlStateLikeObject targetState = testAndGetState(stateName2);
        ArrayList<String> returnNames = sourceState.getNextStateTriggerNames(targetState);
        if (returnNames == null) {
            throw new TransitionNotFoundException(stateMachine.getName(), stateName1, stateName2);
        }
        return returnNames;
    }
}
