package umlparser;

import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.HashMap;

public class MyUmlLifeline {
    private final UmlLifeline lifeline;
    private final HashMap<String, UmlMessage> incomingMessages = new HashMap<>();
    private final HashMap<MessageSort, HashMap<String, UmlMessage>> sentMessages =
            new HashMap<MessageSort, HashMap<String, UmlMessage>>() { {
            put(MessageSort.CREATE_MESSAGE, new HashMap<>());
            put(MessageSort.DELETE_MESSAGE, new HashMap<>());
            put(MessageSort.ASYNCH_CALL, new HashMap<>());
            put(MessageSort.ASYNCH_SIGNAL, new HashMap<>());
            put(MessageSort.REPLY, new HashMap<>());
            put(MessageSort.SYNCH_CALL, new HashMap<>());
        } };

    public MyUmlLifeline(UmlLifeline lifeline) {
        this.lifeline = lifeline;
    }

    public UmlLifeline getLifeline() {
        return lifeline;
    }

    public void addIncomingMessage(UmlMessage message) {
        incomingMessages.put(message.getId(), message);
    }

    public void addSentMessage(UmlMessage message) {
        sentMessages.get(message.getMessageSort()).put(message.getId(), message);
    }

    public int getIncomingMessageCount() {
        return incomingMessages.size();
    }

    public int getSentMessageCount(MessageSort messageType) {
        return sentMessages.get(messageType).size();
    }
}
