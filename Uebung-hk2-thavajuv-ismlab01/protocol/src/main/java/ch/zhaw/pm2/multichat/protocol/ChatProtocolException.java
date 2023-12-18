package ch.zhaw.pm2.multichat.protocol;

/**
 *This exception is thrown after an illegal term is used in the chat log.
 */
public class ChatProtocolException extends Exception {

    public ChatProtocolException(String message) {
        super(message);
    }

}
