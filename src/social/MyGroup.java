package social;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class MyGroup implements Group, Iterable<MyPerson> {
    private final int id;
    private final HashMap<Integer, MyPerson> people = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), (MyPerson) person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (MyPerson p : people.values()) {
            HashMap<MyPerson, Integer> acquaintances = p.getAcquaintances();
            for (MyPerson q : acquaintances.keySet()) {
                if (people.containsKey(q.getId())) {
                    sum += acquaintances.get(q);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        int sum = 0;
        for (Person person : people.values()) {
            sum += person.getAge();
        }
        return sum == 0 ? 0 : sum / people.size();
    }

    @Override
    public int getAgeVar() {
        int var = 0;
        int mean = getAgeMean();
        for (Person person : people.values()) {
            int tmp = person.getAge() - mean;
            var += tmp * tmp;
        }
        return var == 0 ? 0 : var / people.size();
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
    }

    @Override
    public int getSize() {
        return people.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Iterator<MyPerson> iterator() {
        return people.values().iterator();
    }
}
