package umlparser;

import com.oocourse.uml3.interact.common.*;
import com.oocourse.uml3.interact.exceptions.user.*;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.format.UmlGeneralInteraction;
import com.oocourse.uml3.models.common.*;
import com.oocourse.uml3.models.elements.*;
import java.util.*;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private final HashMap<String, MyUmlClass>          classes         = new HashMap<>();
    private final HashMap<String, MyUmlOperation>      operations      = new HashMap<>();
    private final ArrayList<UmlAttribute>              attributes      = new ArrayList<>();
    private final HashMap<String, UmlAssociation>      associations    = new HashMap<>();
    private final HashMap<String, UmlAssociationEnd>   associationEnds = new HashMap<>();
    private final ArrayList<UmlGeneralization>         generalizations = new ArrayList<>();
    private final HashMap<String, MyUmlInterface>      interfaces      = new HashMap<>();
    private final ArrayList<UmlInterfaceRealization>   realizations    = new ArrayList<>();
    private final ArrayList<UmlParameter>              parameters      = new ArrayList<>();
    private final HashMap<String, MyUmlStateMachine>   stateMachines   = new HashMap<>();
    private final HashMap<String, UmlStateLikeObject>  states          = new HashMap<>();
    private final HashMap<String, UmlTransition>       transitions     = new HashMap<>();
    private final HashMap<String, UmlOpaqueBehavior>   behaviors       = new HashMap<>();
    private final HashMap<String, MyUmlInteraction>    interactions    = new HashMap<>();
    private final HashMap<String, MyUmlLifeline>       lifelines       = new HashMap<>();
    private final HashMap<String, UmlMessage>          messages        = new HashMap<>();
    private final HashMap<String, UmlRegion>           regions         = new HashMap<>();
    private final HashMap<String, MyUmlCollaboration>  collaborations  = new HashMap<>();
    private final HashMap<String, UmlEndpoint>         endpoints       = new HashMap<>();
    private final HashMap<String, UmlEvent>            events          = new HashMap<>();

    private void basicStore(UmlElement element) {
        switch (element.getElementType()) {
            case UML_CLASS:
                UmlClass umlClass = (UmlClass) element;
                classes.put(umlClass.getId(), new MyUmlClass(umlClass));
                break;
            case UML_OPERATION:
                UmlOperation operation = (UmlOperation) element;
                operations.put(operation.getId(), new MyUmlOperation(operation));
                break;
            case UML_ATTRIBUTE:
                attributes.add((UmlAttribute) element);
                break;
            case UML_INTERFACE:
                UmlInterface umlInterface = (UmlInterface) element;
                interfaces.put(umlInterface.getId(), new MyUmlInterface(umlInterface));
                break;
            case UML_PARAMETER:
                parameters.add((UmlParameter) element);
                break;
            case UML_ASSOCIATION:
                UmlAssociation association = (UmlAssociation) element;
                associations.put(association.getId(), association);
                break;
            case UML_ASSOCIATION_END:
                UmlAssociationEnd associationEnd = (UmlAssociationEnd) element;
                associationEnds.put(associationEnd.getId(), associationEnd);
                break;
            case UML_GENERALIZATION:
                generalizations.add((UmlGeneralization) element);
                break;
            case UML_INTERFACE_REALIZATION:
                realizations.add((UmlInterfaceRealization) element);
                break;
            default:
        }
    }

    private void stateMachineStore(UmlElement element) {
        switch (element.getElementType()) {
            case UML_STATE_MACHINE:
                UmlStateMachine stateMachine = (UmlStateMachine) element;
                stateMachines.put(stateMachine.getId(), new MyUmlStateMachine(stateMachine));
                break;
            case UML_STATE:
                UmlState state = (UmlState) element;
                states.put(state.getId(), new MyUmlState(state));
                break;
            case UML_FINAL_STATE:
                UmlFinalState finalState = (UmlFinalState) element;
                states.put(finalState.getId(), new MyUmlFinalState(finalState));
                break;
            case UML_PSEUDOSTATE:
                UmlPseudostate pseudoState = (UmlPseudostate) element;
                states.put(pseudoState.getId(), new MyUmlPseudoState(pseudoState));
                break;
            case UML_TRANSITION:
                UmlTransition transition = (UmlTransition) element;
                transitions.put(transition.getId(), transition);
                break;
            case UML_OPAQUE_BEHAVIOR:
                UmlOpaqueBehavior behavior = (UmlOpaqueBehavior) element;
                behaviors.put(behavior.getId(), behavior);
                break;
            case UML_REGION:
                UmlRegion region = (UmlRegion) element;
                regions.put(region.getId(), region);
                break;
            case UML_EVENT:
                UmlEvent event = (UmlEvent) element;
                events.put(event.getId(), event);
                break;
            default:
        }
    }

    private void interactionStore(UmlElement element) {
        switch (element.getElementType()) {
            case UML_INTERACTION:
                UmlInteraction interaction = (UmlInteraction) element;
                interactions.put(interaction.getId(), new MyUmlInteraction(interaction));
                break;
            case UML_LIFELINE:
                UmlLifeline lifeline = (UmlLifeline) element;
                lifelines.put(lifeline.getId(), new MyUmlLifeline(lifeline));
                break;
            case UML_MESSAGE:
                UmlMessage message = (UmlMessage) element;
                messages.put(message.getId(), message);
                break;
            case UML_ENDPOINT:
                UmlEndpoint endpoint = (UmlEndpoint) element;
                endpoints.put(endpoint.getId(), endpoint);
                break;
            default:
        }
    }

    private void basicConstruct() {
        for (UmlInterfaceRealization r : realizations) {
            classes.get(r.getSource()).addDirectInterface(interfaces.get(r.getTarget()));
        }
        for (UmlGeneralization generalization : generalizations) {
            String sourceId = generalization.getSource();
            String targetId = generalization.getTarget();
            MyUmlClass umlClass = classes.get(sourceId);
            if (umlClass != null) { umlClass.setDirectParent(classes.get(targetId)); }
            else { interfaces.get(sourceId).addDirectParent(interfaces.get(targetId)); }
        }
        for (UmlAssociation a : associations.values()) {
            UmlAssociationEnd end1 = associationEnds.get(a.getEnd1());
            UmlAssociationEnd end2 = associationEnds.get(a.getEnd2());
            MyUmlClass class1 = classes.get(end1.getReference());
            MyUmlClass class2 = classes.get(end2.getReference());
            if (class1 == null || class2 == null) { continue; }
            UmlClass c1 = class1.getUmlClass();
            UmlClass c2 = class2.getUmlClass();
            class1.addDirectAssociationNames(c2.getId(), c2.getName());
            class2.addDirectAssociationNames(c1.getId(), c1.getName());
            String end1Name = end1.getName();
            String end2Name = end2.getName();
            if (end2Name != null) { class1.addAssociationEndName(end2.getId(), end2Name); }
            if (end1Name != null) { class2.addAssociationEndName(end1.getId(), end1Name); }
        }
        for (UmlParameter parameter : parameters) {
            operations.get(parameter.getParentId()).addParameter(parameter);
        }
        for (MyUmlOperation operation : operations.values()) {
            String id = operation.getOperation().getParentId();
            MyUmlClass umlClass = classes.get(id);
            if (umlClass != null) { umlClass.addOperation(operation); }
            else { interfaces.get(id).addOperation(operation); }
        }
    }

    private void stateMachineAndInteractionConstruct() {
        for (UmlStateLikeObject s : states.values()) {
            stateMachines.get(regions.get(s.getState().getParentId()).getParentId()).addState(s);
        }
        for (UmlTransition t : transitions.values()) {
            states.get(t.getSource()).addStateWithGuard(states.get(t.getTarget()), t.getGuard());
        }
        for (UmlEvent e : events.values()) {
            UmlTransition t = transitions.get(e.getParentId());
            states.get(t.getSource()).addStateWithTrigger(states.get(t.getTarget()), e.getName());
        }
        for (MyUmlInteraction i : interactions.values()) {
            collaborations.put(i.getInteraction().getParentId(), new MyUmlCollaboration(i));
        }
        for (MyUmlLifeline lifeline : lifelines.values()) {
            interactions.get(lifeline.getLifeline().getParentId()).addLifeline(lifeline);
        }
        for (UmlMessage message : messages.values()) {
            MyUmlLifeline lifeline = lifelines.get(message.getTarget());
            if (lifeline != null) { lifeline.addIncomingMessage(message); }
            lifeline = lifelines.get(message.getSource());
            if (lifeline != null) { lifeline.addSentMessage(message); }
        }
        for (UmlAttribute attribute : attributes) {
            String id = attribute.getParentId();
            MyUmlClass umlClass = classes.get(id);
            if (umlClass != null) { umlClass.addAttribute(attribute); }
            else {
                MyUmlInterface umlInterface = interfaces.get(id);
                if (umlInterface != null) { umlInterface.addAttribute(attribute); }
                else { collaborations.get(id).addAttribute(attribute); }
            }
        }
        for (MyUmlClass umlClass : classes.values()) {
            umlClass.setOperationVisibility();
            umlClass.setInformationNotHidden();
        }
    }

    public MyUmlGeneralInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            switch (element.getElementType()) {
                case UML_CLASS: case UML_OPERATION: case UML_ATTRIBUTE: case UML_INTERFACE:
                case UML_PARAMETER: case UML_ASSOCIATION: case UML_ASSOCIATION_END:
                case UML_GENERALIZATION: case UML_INTERFACE_REALIZATION:
                    basicStore(element);
                    break;
                case UML_STATE_MACHINE: case UML_STATE: case UML_FINAL_STATE: case UML_PSEUDOSTATE:
                case UML_TRANSITION: case UML_OPAQUE_BEHAVIOR: case UML_REGION: case UML_EVENT:
                    stateMachineStore(element);
                    break;
                case UML_INTERACTION: case UML_LIFELINE: case UML_MESSAGE:
                case UML_COLLABORATION: case UML_ENDPOINT:
                    interactionStore(element);
                    break;
                default:
            }
        }
        basicConstruct();
        stateMachineAndInteractionConstruct();
    }

    @Override public int getClassCount() {
        return classes.size();
    }

    MyUmlClass testGetClass(String name) throws ClassNotFoundException, ClassDuplicatedException {
        int count = 0;
        MyUmlClass returnClass = null;
        for (MyUmlClass umlClass : classes.values()) {
            if (umlClass.getUmlClass().getName().equals(name)) {
                if (++count > 1) { throw new ClassDuplicatedException(name); }
                returnClass = umlClass;
            }
        }
        if (returnClass == null) { throw new ClassNotFoundException(name); }
        return returnClass;
    }

    private MyUmlStateMachine testAndGetStateMachine(String name)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        int count = 0;
        MyUmlStateMachine returnStateMachine = null;
        for (MyUmlStateMachine stateMachine : stateMachines.values()) {
            if (stateMachine.getStateMachine().getName().equals(name)) {
                if (++count > 1) { throw new StateMachineDuplicatedException(name); }
                returnStateMachine = stateMachine;
            }
        }
        if (returnStateMachine == null) { throw new StateMachineNotFoundException(name); }
        return returnStateMachine;
    }

    private MyUmlInteraction testAndGetInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        int count = 0;
        MyUmlInteraction returnInteraction = null;
        for (MyUmlInteraction interaction : interactions.values()) {
            if (interaction.getInteraction().getName().equals(interactionName)) {
                if (++count > 1) { throw new InteractionDuplicatedException(interactionName); }
                returnInteraction = interaction;
            }
        }
        if (returnInteraction == null) { throw new InteractionNotFoundException(interactionName); }
        return returnInteraction;
    }

    private String testAndGetTypeName(NameableType type, boolean forOperations) {
        if (type instanceof NamedType) {
            String typeName = ((NamedType) type).getName();
            switch (typeName) {
                case "byte": case "short": case "int": case "long": case "float":
                case "double": case "char": case "boolean": case "String":
                    return typeName;
                case "void": if (forOperations) { return typeName; }
                // else fall through
                default: return null;
            }
        }
        String id = ((ReferenceType) type).getReferenceId();
        MyUmlClass umlClass = classes.get(id);
        String typeName = null;
        if (umlClass != null) { typeName = umlClass.getUmlClass().getName(); }
        else {
            MyUmlInterface umlInterface = interfaces.get(id);
            if (umlInterface != null) { typeName = umlInterface.getInterface().getName(); }
        }
        return typeName;
    }

    @Override public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return testGetClass(className).getOperationCount();
    }

    @Override public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return testGetClass(className).getAttributeCount();
    }

    @Override public Map<Visibility, Integer> getClassOperationVisibility(String className,
            String methodName) throws ClassNotFoundException, ClassDuplicatedException {
        return testGetClass(className).getOperationVisibility(methodName);
    }

    @Override public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
                   AttributeNotFoundException, AttributeDuplicatedException {
        return testGetClass(className).getAttribute(attributeName).getVisibility();
    }

    @Override public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException, AttributeNotFoundException,
                   AttributeDuplicatedException, AttributeWrongTypeException {
        UmlAttribute attribute = testGetClass(className).getAttribute(attributeName);
        String typeName = testAndGetTypeName(attribute.getType(), false);
        if (typeName == null) { throw new AttributeWrongTypeException(className, attributeName); }
        else { return typeName; }
    }

    @Override public List<OperationParamInformation> getClassOperationParamType(String className,
            String methodName) throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyUmlClass umlClass = testGetClass(className);
        HashSet<OperationParamInformation> returnInformation = new HashSet<>();
        for (MyUmlOperation operation : umlClass.getOperations()) {
            UmlOperation o = operation.getOperation();
            if (o.getName().equals(methodName)) {
                String operationId = o.getId();
                ArrayList<String> inTypes = new ArrayList<>();
                String returnType = null;
                for (UmlParameter parameter : operation) {
                    if (parameter.getParentId().equals(operationId)) {
                        String typeName = testAndGetTypeName(parameter.getType(), true);
                        if (typeName == null) {
                            throw new MethodWrongTypeException(className, methodName);
                        } else if (parameter.getDirection().equals(Direction.RETURN)) {
                            returnType = typeName;
                        } else { inTypes.add(typeName); }
                    }
                }
                OperationParamInformation info = new OperationParamInformation(inTypes, returnType);
                if (returnInformation.contains(info)) {
                    throw new MethodDuplicatedException(className, methodName);
                } else { returnInformation.add(info); }
            }
        }
        return new ArrayList<>(returnInformation);
    }

    @Override public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return new ArrayList<>(testGetClass(className).getAssociations().values());
    }

    @Override public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return testGetClass(className).getTopParent().getUmlClass().getName();
    }

    @Override public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        for (MyUmlInterface umlInterface : interfaces.values()) { umlInterface.setVisited(false); }
        return new ArrayList<>(testGetClass(className).setAndGetAllInterfaceNames());
    }

    @Override public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return new ArrayList<>(testGetClass(className).getInformationNotHidden());
    }

    @Override public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return testAndGetStateMachine(stateMachineName).getStateCount();
    }

    @Override public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
                   StateNotFoundException, StateDuplicatedException {
        for (UmlStateLikeObject state : states.values()) { state.setVisited(false); }
        return testAndGetStateMachine(stateMachineName).getSubsequentStateCount(stateName);
    }

    @Override public List<String> getTransitionTrigger(String smName, String source, String target)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
                   StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return testAndGetStateMachine(smName).getTransitionTriggerNames(source, target);
    }

    @Override public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return testAndGetInteraction(interactionName).getParticipantCount();
    }

    @Override public int getIncomingMessageCount(String name, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
                   LifelineNotFoundException, LifelineDuplicatedException {
        return testAndGetInteraction(name).getLifeline(lifelineName).getIncomingMessageCount();
    }

    @Override public int getSentMessageCount(String name, String lifelineName, MessageSort type)
            throws InteractionNotFoundException, InteractionDuplicatedException,
                   LifelineNotFoundException, LifelineDuplicatedException {
        return testAndGetInteraction(name).getLifeline(lifelineName).getSentMessageCount(type);
    }

    @Override public void checkForUml001() throws UmlRule001Exception {
        HashSet<AttributeClassInformation> throwInfo = new HashSet<>();
        for (MyUmlClass umlClass : classes.values()) {
            throwInfo.addAll(umlClass.testAttributeAndAssociationEndNames());
        }
        if (!throwInfo.isEmpty()) {
            throw new UmlRule001Exception(throwInfo);
        }
    }

    @Override public void checkForUml002() throws UmlRule002Exception {
        HashSet<UmlClassOrInterface> returnValue = new HashSet<>();
        for (MyUmlClass umlClass : classes.values()) {
            if (!umlClass.hasBeenVisited()) {
                for (MyUmlClass c : classes.values()) { c.setVisited(false); }
                MyUmlClass.PATH.clear();
                returnValue.addAll(umlClass.testCircularExtension());
            }
        }
        for (MyUmlInterface umlInterface : interfaces.values()) {
            for (MyUmlInterface i : interfaces.values()) {  i.setVisited(false); }
            umlInterface.testCircularExtension(umlInterface.getInterface().getId());
        }
        for (MyUmlInterface umlInterface : interfaces.values()) {
            if (umlInterface.isInCircle()) { returnValue.add(umlInterface.getInterface()); }
        }
        if (!returnValue.isEmpty()) {  throw new UmlRule002Exception(returnValue); }
    }

    @Override public void checkForUml003() throws UmlRule003Exception {
        HashSet<UmlClassOrInterface> throwValue = new HashSet<>();
        for (MyUmlInterface i : interfaces.values()) { i.testDuplicatedExtension(); }
        for (MyUmlInterface i : interfaces.values()) {
            if (i.isDuplicatedExtension()) { throwValue.add(i.getInterface()); }
        }
        if (!throwValue.isEmpty()) { throw new UmlRule003Exception(throwValue); }
    }

    @Override public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClass> throwValue = new HashSet<>();
        for (MyUmlClass umlClass : classes.values()) { umlClass.testDuplicateRealization(); }
        for (MyUmlClass umlClass : classes.values()) {
            if (umlClass.isDuplicatedRealization()) { throwValue.add(umlClass.getUmlClass()); }
        }
        if (!throwValue.isEmpty()) { throw new UmlRule004Exception(throwValue); }
    }

    @Override public void checkForUml005() throws UmlRule005Exception {
        for (MyUmlClass umlClass : classes.values()) { umlClass.testNames(); }
        for (MyUmlInterface umlInterface : interfaces.values()) { umlInterface.testNames(); }
    }

    @Override public void checkForUml006() throws UmlRule006Exception {
        for (MyUmlInterface i : interfaces.values()) { i.testAttributeVisibilities(); }
    }

    @Override public void checkForUml007() throws UmlRule007Exception {
        for (MyUmlCollaboration c : collaborations.values()) { c.testLifelineRepresent(); }
    }

    @Override public void checkForUml008() throws UmlRule008Exception {
        for (UmlStateLikeObject state : states.values()) {
            if (state instanceof MyUmlPseudoState) { ((MyUmlPseudoState) state).testNextStates(); }
        }
        ArrayList<UmlTransition> transitions = new ArrayList<>(this.transitions.values());
        for (int i = 0; i < transitions.size(); i++) {
            UmlTransition transition = transitions.get(i);
            if (states.get(transition.getSource()) instanceof MyUmlPseudoState) {
                for (int j = i + 1; j < transitions.size(); j++) {
                    if (transition.getTarget().equals(transitions.get(j).getTarget())) {
                        throw new UmlRule008Exception();
                    }
                }
            }
        }
    }
}