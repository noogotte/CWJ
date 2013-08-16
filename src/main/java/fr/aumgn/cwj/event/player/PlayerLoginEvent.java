package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;
import fr.aumgn.cwj.event.EventSupport;

public class PlayerLoginEvent extends AbstractPlayerEvent {

    private static final EventSupport support = new EventSupport();

    private Result                    result;

    public PlayerLoginEvent(final Player player) {
        super(player);
        this.result = Result.ALLOWED;
    }

    @Override
    public EventSupport getEventSupport() {
        return support;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Result {

        ALLOWED,

        KICK_FULL,

        KICK_BANNED,

        KICK_WHITELIST,

        KICK_OTHER;
    }
}
