package umlparser;

import com.oocourse.uml3.interact.common.AttributeClassInformation;

import java.util.Objects;

public class ClassAttributeInformation extends AttributeClassInformation {
    private final String classId;

    public ClassAttributeInformation(String attributeName, String className, String classId) {
        super(attributeName, className);
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
        ClassAttributeInformation that = (ClassAttributeInformation) o;
        return classId.equals(that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), classId);
    }
}
