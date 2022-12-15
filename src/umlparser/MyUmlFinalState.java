package umlparser;

import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;

import java.util.ArrayList;
import java.util.Objects;

public class MyUmlFinalState implements UmlStateLikeObject {
    private final UmlFinalState state;
    private boolean visited = false;

    public MyUmlFinalState(UmlFinalState state) {
        this.state = state;
    }

    @Override
    public UmlElement getState() {
        return state;
    }

    @Override
    public void addStateWithGuard(UmlStateLikeObject state, String guardName) {

    }

    @Override
    public void addStateWithTrigger(UmlStateLikeObject state, String name) {

    }

    @Override
    public ArrayList<String> getNextStateTriggerNames(UmlStateLikeObject targetState) {
        return null;
    }

    @Override
    public int traverseNextStates() {
        visited = true;
        return 1;
    }

    @Override
    public int getNextStateCount() {
        return 0;
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
        MyUmlFinalState that = (MyUmlFinalState) o;
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
