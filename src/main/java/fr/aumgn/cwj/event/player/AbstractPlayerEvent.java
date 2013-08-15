package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;
import fr.aumgn.cwj.event.Event;

public abstract class AbstractPlayerEvent extends Event implements PlayerEvent {

    protected final Player player;

    public AbstractPlayerEvent(final Player player) {
        this.player = player;
    }

    public final Player getPlayer() {
        return player;
    }
}
