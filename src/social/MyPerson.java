package social;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Objects;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int socialValue = 0;
    private int balance = 0;
    private final HashMap<MyPerson, Integer> acquaintances = new HashMap<>();
    private final List<Message> messages = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public HashMap<MyPerson, Integer> getAcquaintances() {
        return acquaintances;
    }

    public void addAcquaintance(MyPerson person, int value) {
        acquaintances.put(person, value);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean isLinked(Person person) {
        return id == person.getId() || acquaintances.containsKey((MyPerson) person);
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintances.containsKey((MyPerson) person)) {
            return acquaintances.get(person);  // TODO: check
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getReceivedMessages() {
        if (messages.size() < 4) {
            return messages;
        }
        return messages.subList(0, 4);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public void addMoney(int num) {
        balance += num;
    }

    @Override
    public int getMoney() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MyPerson myPerson = (MyPerson) o;
        return id == myPerson.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
