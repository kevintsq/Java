import java.util.ArrayList;
import java.util.List;

public class Bus {
    private List<Person> personList;

    public Bus() {
        personList = new ArrayList<>();
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }

    public List<Person> getPersonList() {
        // TODO: return the current list of persons in the bus by implementing the deep clone
        List<Person> personList = new ArrayList<>();
        for (Person person : this.personList) {
            personList.add(person.clone());
        }
        return personList;
    }
}
