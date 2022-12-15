package social;

import java.util.Objects;

public class Node implements Comparable<Node> {
    private final MyPerson person;
    private int distance;

    public Node(MyPerson person, int distance) {
        this.person = person;
        this.distance = distance;
    }

    public MyPerson getPerson() {
        return person;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Node node = (Node) o;
        return person.equals(node.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person);
    }

    @Override
    public String toString() {
        return "Node{" + "person=" + person + ", distance=" + distance + '}';
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(distance, o.distance);
    }
}
