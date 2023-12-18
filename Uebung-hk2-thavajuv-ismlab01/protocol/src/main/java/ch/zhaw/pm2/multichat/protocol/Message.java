package ch.zhaw.pm2.multichat.protocol;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message implements Serializable {
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?:@(\\w*))?\\s*(.*)$");

    private String sender;
    private String receiver;
    private DataType type;
    private String payload;

    public static final String USER_ALL = "*";

    /**
     * This constructor sets all parameter
     *
     * @param sender to be set.
     * @param receiver to be set.
     * @param type to be set.
     * @param payload to be set.
     */
    public Message(String sender, String receiver, DataType type, String payload){
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.payload = payload;
    }

    /**
     * This constructor sets all parameter
     *
     * @param dataType to be set.
     * @param payload to be set.
     */
    public Message(DataType dataType, String payload) {
        this.type = dataType;
        this.payload = payload;
    }

    /**
     * This constructor gets message as string and give it to parse to set the parameters.
     *
     * @param message to be set.
     */
    public Message(String message) {
        type = DataType.MESSAGE;
        parseMessage(message);
    }

    /**
     * This constructor sets all parameters
     *
     * @param sender to be set
     * @param receiver to be set
     * @param dataType to be set
     */

    public Message(String sender, String receiver, DataType dataType) {
        Objects.requireNonNull(dataType);
        this.sender = sender;
        this.type = dataType;
        setReceiver(receiver);
    }

    /**
     * The given message is taken apart and the different parameters are assigned.
     * @param message
     */
    private void parseMessage(String message) {
        Matcher matcher = MESSAGE_PATTERN.matcher(message);
        if (matcher.find()) {
            setReceiver(matcher.group(1));
            payload = matcher.group(2);
        }

    }

    private void setReceiver(String receiver) {
        if (receiver == null || receiver.isBlank()) {
            this.receiver = USER_ALL;
        } else {
            this.receiver = receiver;
        }
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getSender(){
        return sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public DataType getType(){
        return type;
    }

    public String getPayload(){
        return payload;
    }

    /**
     * Formats the message according to the message type.
     */
    @Override
    public String toString() {
        switch (getType()) {
            case ERROR:
                return String.format("[ERROR] %s%n", payload);
            case INFO:
                return String.format("[INFO] %s%n", payload);
            case MESSAGE:
                return String.format("[%s -> %s] %s%n", sender, receiver, payload);
            default:
                return getPayload();
        }
    }

    /**
     * Enum representing possible data types and gives information for what this
     * Message is used for
     */
    public enum DataType{
        CONNECT("CONNECT"), CONFIRM("CONFIRM"), DISCONNECT("DISCONNECT"), MESSAGE("MESSAGE"), ERROR("ERROR"), INFO("INFO");
        final String value;

        DataType(final String dataType) {
            value = dataType;
        }
    }

}
