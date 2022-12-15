package umlparser;

import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlInteraction;

import java.util.HashMap;

public class MyUmlInteraction {
    private final UmlInteraction interaction;
    private final HashMap<String, MyUmlLifeline> lifelines = new HashMap<>();

    public HashMap<String, MyUmlLifeline> getLifelines() {
        return lifelines;
    }

    public MyUmlInteraction(UmlInteraction interaction) {
        this.interaction = interaction;
    }

    public void addLifeline(MyUmlLifeline lifeline) {
        lifelines.put(lifeline.getLifeline().getId(), lifeline);
    }

    public UmlInteraction getInteraction() {
        return interaction;
    }

    public int getParticipantCount() {
        return lifelines.size();
    }

    public MyUmlLifeline getLifeline(String lifelineName)
            throws LifelineDuplicatedException, LifelineNotFoundException {
        int count = 0;
        MyUmlLifeline returnLifeline = null;
        for (MyUmlLifeline lifeline : lifelines.values()) {
            if (lifeline.getLifeline().getName().equals(lifelineName)) {
                if (++count > 1) {
                    throw new LifelineDuplicatedException(interaction.getName(), lifelineName);
                }
                returnLifeline = lifeline;
            }
        }
        if (returnLifeline == null) {
            throw new LifelineNotFoundException(interaction.getName(), lifelineName);
        }
        return returnLifeline;
    }
}
