package fr.aumgn.cwj.event;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import fr.aumgn.cwj.utils.UnexpectedException;

public class EventSupport<E extends Event<E>> {

    private final List<RegisteredHandler<E>> handleHandlers;
    private final List<RegisteredHandler<E>> preMonitorHandlers;
    private final List<RegisteredHandler<E>> postMonitorHandlers;

    public EventSupport() {
        this.handleHandlers = Lists.newArrayList();
        this.preMonitorHandlers = Lists.newArrayList();
        this.postMonitorHandlers = Lists.newArrayList();
    }

    List<RegisteredHandler<E>> getHandlers(EventPhase phase) {
        switch (phase) {
        case HANDLE:
            return handleHandlers;
        case PRE_MONITOR:
            return preMonitorHandlers;
        case POST_MONITOR:
            return postMonitorHandlers;
        }

        throw new UnexpectedException();
    }

    void addHandler(EventPhase phase, RegisteredHandler<E> handler) {
        List<RegisteredHandler<E>> handlers = getHandlers(phase);
        handlers.add(handler);
        Collections.sort(handlers);
    }
}
