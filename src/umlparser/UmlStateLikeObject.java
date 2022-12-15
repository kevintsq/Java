package umlparser;

import com.oocourse.uml3.models.elements.UmlElement;

import java.util.ArrayList;

public interface UmlStateLikeObject {
    UmlElement getState();

    void addStateWithGuard(UmlStateLikeObject state, String guardName);

    void addStateWithTrigger(UmlStateLikeObject state, String triggerName);

    ArrayList<String> getNextStateTriggerNames(UmlStateLikeObject targetState);

    int traverseNextStates();

    int getNextStateCount();

    boolean hasBeenVisited();

    void setVisited(boolean b);

    @Override
    int hashCode();
}
