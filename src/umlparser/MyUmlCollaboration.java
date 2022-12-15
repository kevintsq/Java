package umlparser;

import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.models.elements.UmlAttribute;

import java.util.HashMap;

public class MyUmlCollaboration {
    private MyUmlInteraction interaction;
    private final HashMap<String, UmlAttribute> attributes = new HashMap<>();

    public MyUmlCollaboration(MyUmlInteraction interaction) {
        this.interaction = interaction;
    }

    public void setInteraction(MyUmlInteraction interaction) {
        this.interaction = interaction;
    }

    public HashMap<String, UmlAttribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.put(attribute.getId(), attribute);
    }

    public void testLifelineRepresent() throws UmlRule007Exception {
        for (MyUmlLifeline lifeline : interaction.getLifelines().values()) {
            if (attributes.get(lifeline.getLifeline().getRepresent()) == null) {
                throw new UmlRule007Exception();
            }
        }
    }
}
