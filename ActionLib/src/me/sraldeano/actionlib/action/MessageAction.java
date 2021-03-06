package me.sraldeano.actionlib.action;

import me.sraldeano.actionlib.Action;

/**
 *
 * @author markelm
 */
public class MessageAction extends Action {

    public String message = "Default Message";
    
    public MessageAction() {
        super("Message");
    }

    @Override
    public void onExecute() {
        getPlayer().sendMessage(replaceText(message));
    }
    
}
