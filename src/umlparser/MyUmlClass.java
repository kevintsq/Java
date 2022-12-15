package umlparser;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class MyUmlClass {
    private final UmlClass umlClass;
    private boolean visited = false;
    private boolean duplicateRealization = false;
    public static final Stack<UmlClass> PATH = new Stack<>();
    private final ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private final HashSet<ClassAttributeInformation> informationNotHidden = new HashSet<>();
    private final ArrayList<MyUmlOperation> operations = new ArrayList<>();
    private final HashMap<String, HashMap<Visibility, Integer>> visibilityStat = new HashMap<>();
    private final HashMap<String, String> directAssociationNames = new HashMap<>();
    private final HashMap<String, MyUmlInterface> directInterfaces = new HashMap<>();
    private final HashSet<String> allInterfaceNames = new HashSet<>();
    private final HashMap<String, String> associationEndNames = new HashMap<>();
    private MyUmlClass directParent = null;
    private static final HashMap<Visibility, Integer> EMPTY_ATTRIBUTE_VISIBILITY =
            new HashMap<Visibility, Integer>() {
            {
                put(Visibility.PUBLIC, 0);
                put(Visibility.PRIVATE, 0);
                put(Visibility.PROTECTED, 0);
                put(Visibility.PACKAGE, 0);
            }
        };

    public void addAssociationEndName(String id, String name) {
        associationEndNames.put(id, name);
    }

    public HashSet<AttributeClassInformation> testAttributeAndAssociationEndNames() {
        HashSet<AttributeClassInformation> returnInfo = new HashSet<>();
        HashSet<String> tmpNames = new HashSet<>();
        for (UmlAttribute attribute : attributes) {
            String name = attribute.getName();
            if (name != null) {
                if (tmpNames.contains(name)) {
                    returnInfo.add(new AttributeClassInformation(name, umlClass.getName()));
                }
                tmpNames.add(name);
            }
        }
        for (String name : associationEndNames.values()) {
            if (name != null) {
                if (tmpNames.contains(name)) {
                    returnInfo.add(new AttributeClassInformation(name, umlClass.getName()));
                }
                tmpNames.add(name);
            }
        }
        return returnInfo;
    }

    public void testNames() throws UmlRule005Exception {
        if (umlClass.getName() == null) {
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

    public HashSet<UmlClassOrInterface> testCircularExtension() {
        HashSet<UmlClassOrInterface> returnNames = new HashSet<>();
        if (visited) {
            for (UmlClass tmpClass = PATH.pop(); tmpClass != umlClass; tmpClass = PATH.pop()) {
                returnNames.add(tmpClass);
            }
            returnNames.add(umlClass);
            return returnNames;
        } else if (directParent == null) {
            return returnNames;
        } else {
            visited = true;
            PATH.push(umlClass);
            return directParent.testCircularExtension();
        }
    }

    public MyUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
    }

    private UmlAttribute testAndGetAttribute(String name, int initialCount, String originClass)
            throws AttributeDuplicatedException {
        int count = initialCount;
        UmlAttribute returnAttribute = null;
        for (UmlAttribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                if (++count > 1) {
                    throw new AttributeDuplicatedException(originClass, name);
                }
                returnAttribute = attribute;
            }
        }
        if (directParent != null) {
            if (returnAttribute == null) {
                returnAttribute = directParent.testAndGetAttribute(name, count, originClass);
            } else {
                directParent.testAndGetAttribute(name, count, originClass);
            }
        }
        return returnAttribute;
    }

    public UmlAttribute getAttribute(String name)
            throws AttributeDuplicatedException, AttributeNotFoundException {
        UmlAttribute returnAttribute = testAndGetAttribute(name, 0, umlClass.getName());
        if (returnAttribute == null) {
            throw new AttributeNotFoundException(umlClass.getName(), name);
        }
        return returnAttribute;
    }

    public int getAttributeCount() {
        if (directParent == null) {
            return attributes.size();
        } else {
            return attributes.size() + directParent.getAttributeCount();
        }
    }

    public void addOperation(MyUmlOperation operation) {
        operations.add(operation);
    }

    public ArrayList<MyUmlOperation> getOperations() {
        return operations;
    }

    public int getOperationCount() {
        return operations.size();
    }

    public void setOperationVisibility() {
        for (MyUmlOperation o : operations) {
            UmlOperation operation = o.getOperation();
            visibilityStat.merge(operation.getName(), new HashMap<Visibility, Integer>() {
                {
                    put(operation.getVisibility(), 1);
                }
                }, (stat, ignored) -> {
                    stat.merge(operation.getVisibility(), 1, Integer::sum);
                    return stat;
                }
            );
        }
    }

    public HashMap<Visibility, Integer> getOperationVisibility(String name) {
        HashMap<Visibility, Integer> returnInfo = visibilityStat.get(name);
        if (returnInfo == null) {
            returnInfo = EMPTY_ATTRIBUTE_VISIBILITY;
        }
        return returnInfo;
    }

    public void addDirectAssociationNames(String id, String name) {
        directAssociationNames.put(id, name);
    }

    public HashMap<String, String> getAssociations() {
        if (directParent == null) {
            return directAssociationNames;
        } else {
            HashMap<String, String> associations = new HashMap<>(directAssociationNames);
            associations.putAll(directParent.getAssociations());
            return associations;
        }
    }

    public void addDirectInterface(MyUmlInterface umlInterface) {
        String name = umlInterface.getInterface().getName();
        if (directInterfaces.containsKey(name)) {
            duplicateRealization = true;
        } else {
            directInterfaces.put(name, umlInterface);
        }
    }

    public HashSet<String> setAndGetAllInterfaceNames() {
        if (allInterfaceNames.isEmpty()) {
            for (MyUmlInterface umlInterface : directInterfaces.values()) {
                allInterfaceNames.addAll(umlInterface.setAndGetAllInterfaceNames());
            }
            if (directParent != null) {
                allInterfaceNames.addAll(directParent.setAndGetAllInterfaceNames());
            }
        }
        return allInterfaceNames;
    }

    public void testDuplicateRealization() {
        if (directInterfaces.isEmpty()) {
            return;
        }
        setAndGetAllInterfaceNames();
        ArrayList<MyUmlInterface> interfaces = new ArrayList<>(directInterfaces.values());
        HashSet<String> intersection =
                new HashSet<>(interfaces.get(0).setAndGetAllInterfaceNames());
        for (int i = 1; i < interfaces.size(); i++) {
            intersection.retainAll(interfaces.get(i).setAndGetAllInterfaceNames());
            if (!intersection.isEmpty()) {
                duplicateRealization = true;
                return;
            }
        }
        if (directParent == null) {
            return;
        }
        MyUmlClass tmpParent = directParent;
        while (tmpParent.directParent != null) {
            tmpParent = tmpParent.directParent;
        }
        for (MyUmlInterface umlInterface : directInterfaces.values()) {
            for (MyUmlInterface parent : tmpParent.directInterfaces.values()) {
                intersection = new HashSet<>(umlInterface.setAndGetAllInterfaceNames());
                intersection.retainAll(parent.setAndGetAllInterfaceNames());
                if (!intersection.isEmpty()) {
                    duplicateRealization = true;
                    return;
                }
            }
        }
    }

    public void setDirectParent(MyUmlClass directParent) {
        this.directParent = directParent;
    }

    public MyUmlClass getTopParent() {
        if (directParent == null) {
            return this;
        } else {
            return directParent.getTopParent();
        }
    }

    public void setInformationNotHidden() {
        for (UmlAttribute attribute : attributes) {
            if (!attribute.getVisibility().equals(Visibility.PRIVATE)) {
                informationNotHidden.add(new ClassAttributeInformation(
                        attribute.getName(), umlClass.getName(), umlClass.getId()));
            }
        }
    }

    public HashSet<ClassAttributeInformation> getInformationNotHidden() {
        if (directParent == null) {
            return informationNotHidden;
        } else {
            HashSet<ClassAttributeInformation> info = new HashSet<>(informationNotHidden);
            info.addAll(directParent.getInformationNotHidden());
            return info;
        }
    }

    @Override
    public String toString() {
        return umlClass.toString();
    }

    public void setVisited(boolean b) {
        visited = b;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public boolean isDuplicatedRealization() {
        if (duplicateRealization) {
            return true;
        }
        if (directParent != null) {
            MyUmlClass tmpParent = directParent;
            while (tmpParent.directParent != null) {
                if (tmpParent.duplicateRealization) {
                    return true;
                }
                tmpParent = tmpParent.directParent;
            }
        }
        for (MyUmlInterface parent : directInterfaces.values()) {
            if (parent.isDuplicatedExtension()) {
                return true;
            }
        }
        return false;
    }
}
