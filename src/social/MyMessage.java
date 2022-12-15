package social;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.Objects;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private Person person2;
    private Group group;

    public MyMessage(int messageId,
                     int messageSocialValue,
                     Person messagePerson1,
                     Person messagePerson2) {
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = messagePerson2;
        type = 0;
    }

    public MyMessage(int messageId,
                     int messageSocialValue,
                     Person messagePerson1,
                     Group messageGroup) {
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        group = messageGroup;
        type = 1;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MyMessage myMessage = (MyMessage) o;
        return id == myMessage.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
