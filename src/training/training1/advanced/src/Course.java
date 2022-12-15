package training.training1.advanced.src;

import java.util.Objects;

public class Course {
    private final String id;
    private final String name;

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Course course = (Course) o;
        return id.equals(course.id) &&
                name.equals(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
