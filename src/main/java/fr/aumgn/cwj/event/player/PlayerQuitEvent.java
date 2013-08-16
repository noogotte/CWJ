package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;
import fr.aumgn.cwj.event.EventSupport;

public class PlayerQuitEvent extends AbstractPlayerEvent {

    private static final EventSupport support = new EventSupport();

    private String                    quitMessage;

    public PlayerQuitEvent(final Player player, final String quitMessage) {
        super(player);
        this.quitMessage = quitMessage;
    }

    @Override
    public EventSupport getEventSupport() {
        return support;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }
}
