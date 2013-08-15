package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;

public class PlayerQuitEvent extends AbstractPlayerEvent {

    private String quitMessage;

    public PlayerQuitEvent(final Player player, final String quitMessage) {
        super(player);
        this.quitMessage = quitMessage;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }
}
