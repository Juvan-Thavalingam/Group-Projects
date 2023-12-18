package ch.zhaw.pm2.multichat.client.ui;

import ch.zhaw.pm2.multichat.protocol.Message;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

/**
 * Extends the message class to get processed message with colors.
 */
public class HighlightedMessage extends Message{

    public HighlightedMessage(Message message){
        super(message.getSender(), message.getReceiver(), message.getType(), message.getPayload());
    }

    /**
     * Highlighting of a message is based on the {@link DataType}
     * @return a highlighted text
     */
    public Text getMessageText(){
        Text messageText = new Text(toString());

        switch (getType()) {
            case ERROR:
                messageText.setFill(Color.RED);
                break;
            case INFO:
                messageText.setFill(Color.ORANGE);
                break;
            default:
                messageText.setFill(Color.BLACK);
                break;
        }

        return messageText;
    }
}
