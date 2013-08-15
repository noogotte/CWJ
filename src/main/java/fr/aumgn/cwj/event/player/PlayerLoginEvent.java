package fr.aumgn.cwj.event.player;

import fr.aumgn.cwj.Player;

public class PlayerLoginEvent extends AbstractPlayerEvent {

    private Result result;

    public PlayerLoginEvent(final Player player) {
        super(player);
        this.result = Result.ALLOWED;
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
