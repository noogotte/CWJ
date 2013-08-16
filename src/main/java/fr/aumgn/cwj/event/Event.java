package fr.aumgn.cwj.event;

public abstract class Event<E extends Event<E>> {

    /**
     * Returns the {@link EventSupport} for this Event.
     * <p>
     * Implementing this method means that the Event is actually meant to be
     * listened to.
     */
    public abstract EventSupport<E> getEventSupport();

    /**
     * This is called before {@link EventPhase.MONITOR} and
     * {@link EventPhase.POST_MONITOR} in order to allow implementation to
     * enforce immutability.
     * <p>
     * Overriding this method is not necessary because EventPhase is before all
     * a contract between the API and the users.
     */
    protected void freeze() {
    }
}
