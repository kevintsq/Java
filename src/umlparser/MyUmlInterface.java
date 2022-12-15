package umlparser;

import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class MyUmlInterface {
    private final UmlInterface umlInterface;
    private boolean visited = false;
    private boolean inCircle = false;
    private boolean duplicatedExtension = false;
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final ArrayList<MyUmlOperation> operations = new ArrayList<>();
    private final ArrayList<MyUmlInterface> directParents = new ArrayList<>();
    private final HashSet<String> allInterfaceNames = new HashSet<>();

    public void testNames() throws UmlRule005Exception {
        if (umlInterface.getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlAttribute attribute : attributes) {
            if (attribute.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        for (MyUmlOperation operation : operations) {
            operation.testNames();
        }
    }

    public boolean isInCircle() {
        return inCircle;
    }

    public void testDuplicatedExtension() {
        setAndGetAllInterfaceNames();
        for (int i = 0; i < directParents.size(); i++) {
            for (int j = i + 1; j < directParents.size(); j++) {
                HashSet<String> intersect = new HashSet<>(directParents.get(i).allInterfaceNames);
                intersect.retainAll(directParents.get(j).allInterfaceNames);
                if (!intersect.isEmpty()) {
                    duplicatedExtension = true;
                    return;
                }
            }
        }
    }

    public boolean isDuplicatedExtension() {
        if (duplicatedExtension) {
            return true;
        }
        for (MyUmlInterface parent : directParents) {
            if (parent.isDuplicatedExtension()) {
                return true;
            }
        }
        return false;
    }

    public void testCircularExtension(String fromId) {
        if (visited) {
            if (umlInterface.getId().equals(fromId)) {
                inCircle = true;
            }
        } else if (!directParents.isEmpty()) {
            visited = true;
            for (MyUmlInterface umlInterface : directParents) {
                umlInterface.testCircularExtension(fromId);
            }
        }
    }

    public MyUmlInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public UmlInterface getInterface() {
        return umlInterface;
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
    }

    public void testAttributeVisibilities() throws UmlRule006Exception {
        for (UmlAttribute attribute : attributes) {
            if (!attribute.getVisibility().equals(Visibility.PUBLIC)) {
                throw new UmlRule006Exception();
            }
        }
    }

    public void addOperation(MyUmlOperation operation) {
        operations.add(operation);
    }

    public void addDirectParent(MyUmlInterface myUmlInterface) {
        directParents.add(myUmlInterface);
    }

    public HashSet<String> setAndGetAllInterfaceNames() {
        if (allInterfaceNames.isEmpty()) {
            for (MyUmlInterface umlInterface : directParents) {
                allInterfaceNames.addAll(umlInterface.setAndGetAllInterfaceNames());
            }
        }
        allInterfaceNames.add(umlInterface.getName());
        return allInterfaceNames;
    }

    public void setVisited(boolean b) {
        visited = b;
    }

    public boolean hasBeenVisited() {
        return visited;
    }
}
