package training.training1.advanced.src;

import java.util.Objects;

public class CourseSelectionRecord {
    private final Student student;
    private final Course course;

    public CourseSelectionRecord(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CourseSelectionRecord that = (CourseSelectionRecord) o;
        return student.equals(that.student) &&
                course.equals(that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return student.toString() + " selects " + course.toString();
    }
}
