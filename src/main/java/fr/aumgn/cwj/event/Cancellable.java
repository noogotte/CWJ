package fr.aumgn.cwj.event;

public interface Cancellable {

    public boolean isCancelled();

    public void setCancelled(boolean cancelled);
}
