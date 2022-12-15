package social;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {
    private final int money;

    public MyRedEnvelopeMessage(int messageId,
                                int messageMoney,
                                Person messagePerson1,
                                Person messagePerson2) {
        super(messageId, messageMoney * 5, messagePerson1, messagePerson2);
        money = messageMoney;
    }

    public MyRedEnvelopeMessage(int messageId,
                                int messageMoney,
                                Person messagePerson1,
                                Group messageGroup) {
        super(messageId, messageMoney * 5, messagePerson1, messageGroup);
        money = messageMoney;
    }

    @Override
    public int getMoney() {
        return money;
    }
}
