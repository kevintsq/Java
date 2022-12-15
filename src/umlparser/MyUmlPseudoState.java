package umlparser;

import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlPseudostate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyUmlPseudoState implements UmlStateLikeObject {
    private final UmlPseudostate state;
    private boolean visited = false;
    private final HashMap<UmlStateLikeObject, ArrayList<String>>
            nextStatesWithTriggerNames = new HashMap<>();
    private final HashMap<UmlStateLikeObject, ArrayList<String>>
            nextStatesWithGuardNames = new HashMap<>();

    public MyUmlPseudoState(UmlPseudostate state) {
        this.state = state;
    }

    @Override
    public UmlElement getState() {
        return state;
    }

    public void testNextStates() throws UmlRule008Exception {
        if (nextStatesWithGuardNames.size() > 1 || nextStatesWithTriggerNames.size() > 1) {
            throw new UmlRule008Exception();
        }
        for (ArrayList<String> guardNames : nextStatesWithGuardNames.values()) {
            if (!guardNames.isEmpty()) {
                throw new UmlRule008Exception();
            }
        }
        for (ArrayList<String> triggerNames : nextStatesWithTriggerNames.values()) {
            if (!triggerNames.isEmpty()) {
                throw new UmlRule008Exception();
            }
        }
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
    public ArrayList<String> getNextStateTriggerNames(UmlStateLikeObject targetState) {
        return nextStatesWithTriggerNames.get(targetState);
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

    @Override
    public boolean hasBeenVisited() {
        return visited;
    }

    @Override
    public void setVisited(boolean b) {
        visited = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MyUmlPseudoState that = (MyUmlPseudoState) o;
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
