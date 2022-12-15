package umlparser;

import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyUmlState implements UmlStateLikeObject {
    private final UmlState state;
    private boolean visited = false;
    private final HashMap<UmlStateLikeObject, ArrayList<String>>
            nextStatesWithTriggerNames = new HashMap<>();
    private final HashMap<UmlStateLikeObject, ArrayList<String>>
            nextStatesWithGuardNames = new HashMap<>();

    public MyUmlState(UmlState state) {
        this.state = state;
    }

    @Override
    public UmlState getState() {
        return state;
    }

    @Override
    public void addStateWithTrigger(UmlStateLikeObject state, String triggerName) {
        nextStatesWithTriggerNames.merge(state, new ArrayList<String>() {
            { if (triggerName != null) { add(triggerName); } }
        }, (triggers, ignored) -> {
                if (triggerName != null) {
                    triggers.add(triggerName);
                }
                return triggers;
            });
    }

    @Override
    public void addStateWithGuard(UmlStateLikeObject state, String guardName) {
        nextStatesWithGuardNames.merge(state, new ArrayList<String>() {
            { if (guardName != null) { add(guardName); } }
        }, (triggers, ignored) -> {
                if (guardName != null) {
                    triggers.add(guardName);
                }
                return triggers;
            });
    }

    @Override
    public boolean hasBeenVisited() {
        return visited;
    }

    @Override
    public void setVisited(boolean b) {
        visited = b;
    }

    @Override
    public int traverseNextStates() {
        visited = true;
        int count = 1;
        for (UmlStateLikeObject state : nextStatesWithGuardNames.keySet()) {
            if (!state.hasBeenVisited()) {
                count += state.traverseNextStates();
            }
        }
        return count;
    }

    @Override
    public int getNextStateCount() {
        int count = 0;
        for (UmlStateLikeObject state : nextStatesWithGuardNames.keySet()) {
            if (!state.hasBeenVisited()) {
                count += state.traverseNextStates();
            }
        }
        return count;
    }

    public ArrayList<String> getNextStateTriggerNames(UmlStateLikeObject targetState) {
        return nextStatesWithTriggerNames.get(targetState);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MyUmlState that = (MyUmlState) o;
        return state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
