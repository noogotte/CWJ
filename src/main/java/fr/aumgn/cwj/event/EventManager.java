package fr.aumgn.cwj.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import fr.aumgn.cwj.CWJ;

public class EventManager {

    public <E extends Event<E>> void register(Class<E> eventClass, EventPhase phase, EventOrder order,
            EventExecutor<E> executor) {
        RegisteredHandler<E> registered = new RegisteredHandler<E>(order, executor);
        EventSupport<E> eventSupport = eventSupport(eventClass);
        eventSupport.addHandler(phase, registered);
    }

    private <E extends Event<E>> EventSupport<E> eventSupport(Class<E> eventClass) {
        try {
            Field field = eventClass.getDeclaredField("eventSupport");
            if (!Modifier.isStatic(field.getModifiers())) {
                CWJ.getLogger().log(Level.WARNING,
                        "Field `eventSupport` is not static for Event " + eventClass.getName());
                return null;
            }
            if (!EventSupport.class.isAssignableFrom(field.getType())) {
                System.out.println(field.getType());
                CWJ.getLogger().log(Level.WARNING,
                        "Field `eventSupport` is not an instance of `EventSupport`" + eventClass.getName());
                return null;
            }

            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            EventSupport<E> support = (EventSupport<E>) field.get(null);
            return support;
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException exc) {
            CWJ.getLogger().log(Level.WARNING,
                    "Unable to access `eventSupport` field for Event " + eventClass.getName(), exc);
        }

        return null;
    }

    public <E extends Event<E>> void firePreProcess(E event) {
        for (RegisteredHandler<E> handler : event.getEventSupport().getHandlers(EventPhase.HANDLE)) {
            handler.getExecutor().execute(event);
        }
        event.freeze();
        for (RegisteredHandler<E> handler : event.getEventSupport().getHandlers(EventPhase.PRE_MONITOR)) {
            handler.getExecutor().execute(event);
        }
    }

    public <E extends Event<E>> void firePostProcess(E event) {
        for (RegisteredHandler<E> handler : event.getEventSupport().getHandlers(EventPhase.POST_MONITOR)) {
            handler.getExecutor().execute(event);
        }
    }
}
