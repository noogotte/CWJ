package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;
import fr.aumgn.cwj.event.EventSupport;

public class PlayerQuitEvent extends AbstractPlayerEvent<PlayerLoginEvent> {

    private static final EventSupport<PlayerLoginEvent> eventSupport = new EventSupport<PlayerLoginEvent>();

    private String                                      quitMessage;

    public PlayerQuitEvent(final Player player, final String quitMessage) {
        super(player);
        this.quitMessage = quitMessage;
    }

    @Override
    public EventSupport<PlayerLoginEvent> getEventSupport() {
        return eventSupport;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }
}
