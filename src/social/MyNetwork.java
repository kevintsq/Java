package social;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    static final int MAX_PEOPLE_IN_GROUP = 1111;
    private final HashMap<Integer, MyPerson> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashMap<Integer, Integer> emojiHeat = new HashMap<>();
    private final HashMap<Integer, Boolean> visited = new HashMap<>();
    private int addPersonCnt = 0;
    private int lastAddPersonCnt = 0;
    private int addRelationCnt = 0;
    private int lastAddRelationCnt = 0;
    private int lastBlockCnt = 0;

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int id = person.getId();
        if (people.containsKey(id)) {
            throw new IdenticalPersonIdException(id);
        } else {
            people.put(id, (MyPerson) person);
            visited.put(id, false);
            addPersonCnt++;
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (people.get(id1).isLinked(people.get(id2))) {
            throw new IdenticalRelationException(id1, id2);
        } else {
            MyPerson person1 = people.get(id1);
            MyPerson person2 = people.get(id2);
            person1.addAcquaintance(person2, value);
            person2.addAcquaintance(person1, value);
            addRelationCnt++;
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!people.get(id1).isLinked(people.get(id2))) {
            throw new MyRelationNotFountException(id1, id2);
        } else {
            return people.get(id1).queryValue(people.get(id2));
        }
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return people.get(id1).compareTo(people.get(id2));
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (people.containsKey(id)) {
            int rank = 1;
            for (Person person : people.values()) {
                if (compareName(id, person.getId()) > 0) {
                    rank++;
                }
            }
            return rank;
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    private boolean dfs(int src, int dst) {
        if (src == dst) {
            return true;
        }
        visited.put(src, true);
        for (Person person : people.get(src).getAcquaintances().keySet()) {
            int id = person.getId();
            if (!visited.get(id)) {
                if (dfs(id, dst)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void dfs(int src) {
        visited.put(src, true);
        for (Person person : people.get(src).getAcquaintances().keySet()) {
            int id = person.getId();
            if (!visited.get(id)) {
                dfs(id);
            }
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            visited.replaceAll((k, v) -> false);
            return dfs(id1, id2);  // recursive
        }
    }

    @Override
    public int queryBlockSum() {
        if (lastAddPersonCnt == addPersonCnt && lastAddRelationCnt == addRelationCnt) {
            return lastBlockCnt;
        }
        int sum = 0;
        visited.replaceAll((k, v) -> false);
        for (Map.Entry<Integer, Boolean> isVisited : visited.entrySet()) {
            if (!isVisited.getValue()) {
                dfs(isVisited.getKey());
                sum++;
            }
        }
        lastAddPersonCnt = addPersonCnt;
        lastAddRelationCnt = addRelationCnt;
        lastBlockCnt = sum;
        return sum;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        int id = group.getId();
        if (groups.containsKey(id)) {
            throw new IdenticalGroupIdException(id);
        }
        groups.put(id, group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int personId, int groupId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(groupId)) {
            throw new MyGroupIdNotFoundException(groupId);
        }
        if (!people.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        Group group = groups.get(groupId);
        Person person = people.get(personId);
        if (group.hasPerson(person)) {
            throw new IdenticalPersonIdException(personId);
        }
        if (group.getSize() < MAX_PEOPLE_IN_GROUP) {
            group.addPerson(person);
        }
    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeMean();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int personId, int groupId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(groupId)) {
            throw new MyGroupIdNotFoundException(groupId);
        }
        if (!people.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        Group group = groups.get(groupId);
        Person person = people.get(personId);
        if (!group.hasPerson(person)) {
            throw new IdenticalPersonIdException(personId);
        }
        groups.get(groupId).delPerson(people.get(personId));
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message)
            throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        int id = message.getId();
        if (messages.containsKey(id)) {
            throw new IdenticalMessageIdException(id);
        }
        int emojiId;
        if (message instanceof EmojiMessage &&
                !emojiHeat.containsKey(emojiId = ((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(emojiId);
        }
        Person person1 = message.getPerson1();
        if (message.getType() == 0 && person1.equals(message.getPerson2())) {
            throw new IdenticalPersonIdException(person1.getId());
        }
        messages.put(id, message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = messages.get(id);
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        if (message.getType() == 0) {
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFountException(person1.getId(), person2.getId());
            }
            int socialValue = message.getSocialValue();
            person1.addSocialValue(socialValue);
            person2.addSocialValue(socialValue);
            person2.getMessages().add(0, message);
            if (message instanceof RedEnvelopeMessage) {
                int money = ((RedEnvelopeMessage) message).getMoney();
                person1.addMoney(-money);
                person2.addMoney(money);
            }
        } else {
            if (!message.getGroup().hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            MyGroup group = (MyGroup) message.getGroup();
            for (Person person : group) {
                person.addSocialValue(message.getSocialValue());
            }
            if (message instanceof RedEnvelopeMessage) {
                int size = group.getSize();
                int money = ((RedEnvelopeMessage) message).getMoney() / size;
                person1.addMoney(-money * size);
                for (Person person : group) {
                    person.addMoney(money);
                }
            }
        }
        if (message instanceof EmojiMessage) {
            emojiHeat.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        }
        messages.remove(id);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiHeat.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiHeat.containsKey(id)) {
            throw new IdenticalEmojiIdException(id);
        }
        emojiHeat.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiHeat.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeat.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        messages.values().removeIf(message ->
            message instanceof EmojiMessage && 
            emojiHeat.get(((EmojiMessage) message).getEmojiId()) < limit);
        emojiHeat.values().removeIf(heat -> heat < limit);
        return emojiHeat.size();
    }

    private int dijkstra(Person src, Person dst) {
        HashMap<MyPerson, Integer> distances = new HashMap<>();
        PriorityQueue<Node> unvisited = new PriorityQueue<>();
        for (MyPerson person : people.values()) {
            if (!person.equals(src)) {
                distances.put(person, Integer.MAX_VALUE);
                unvisited.offer(new Node(person, Integer.MAX_VALUE));
            }
        }
        distances.put((MyPerson) src, 0);
        unvisited.offer(new Node((MyPerson) src, 0));
        while (!unvisited.isEmpty()) {
            Node smallest = unvisited.poll();
            MyPerson smallestPerson = smallest.getPerson();
            int smallestDistance = smallest.getDistance();
            if (distances.get(smallestPerson) < smallestDistance) {
                continue;
            }
            for (Map.Entry<MyPerson, Integer> entry :
                    smallestPerson.getAcquaintances().entrySet()) {
                int distance = smallestDistance + entry.getValue();
                MyPerson person = entry.getKey();
                if (distance < distances.get(person)) {
                    distances.put(person, distance);
                    unvisited.offer(new Node(person, distance));
                }
            }
        }
        return distances.get((MyPerson) dst);
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        Message message;
        if (!messages.containsKey(id) || (message = messages.get(id)).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        try {
            if (!isCircle(person1.getId(), person2.getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {  // this should never happen
            e.print();
            return -1;
        }
        messages.remove(id);
        person2.getMessages().add(0, message);
        int socialValue = message.getSocialValue();
        person1.addSocialValue(socialValue);
        person2.addSocialValue(socialValue);
        if (message instanceof EmojiMessage) {
            emojiHeat.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        } else if (message instanceof RedEnvelopeMessage) {
            int money = ((RedEnvelopeMessage) message).getMoney();
            person1.addMoney(-money);
            person2.addMoney(money);
        }
        return dijkstra(person1, person2);
    }
}