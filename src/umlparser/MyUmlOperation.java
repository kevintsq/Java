package umlparser;

import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Iterator;

public class MyUmlOperation implements Iterable<UmlParameter> {
    private final UmlOperation operation;
    private final ArrayList<UmlParameter> parameters = new ArrayList<>();

    public void testNames() throws UmlRule005Exception {
        if (operation.getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlParameter parameter : parameters) {
            if (!parameter.getDirection().equals(Direction.RETURN) && parameter.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
    }

    public MyUmlOperation(UmlOperation operation) {
        this.operation = operation;
    }

    public UmlOperation getOperation() {
        return operation;
    }

    public void addParameter(UmlParameter parameter) {
        parameters.add(parameter);
    }

    public ArrayList<UmlParameter> getParameters() {
        return parameters;
    }

    @Override
    public Iterator<UmlParameter> iterator() {
        return parameters.iterator();
    }
}
