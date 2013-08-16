package fr.aumgn.cwj.event;

public enum EventPhase {

    /**
     * First phase. This is the one during which handlers must modify the event
     * if they want to.
     */
    HANDLE,

    /**
     * Second phase, called just before the event is processed.
     * <p>
     * No modification should occur during this phase. Note that, this may be
     * enforced by the Event implementation with {@link Event#freeze()}
     */
    PRE_MONITOR,

    /**
     * Last phase, called just after the event has been processed.
     * <p>
     * No modification should occur during this phase. Note that, this may be
     * enforced by the Event implementation with {@link Event#freeze()}
     */
    POST_MONITOR;
}
