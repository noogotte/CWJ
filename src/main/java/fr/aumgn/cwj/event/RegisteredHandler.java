package fr.aumgn.cwj.event;

public class RegisteredHandler<E extends Event<E>> implements Comparable<RegisteredHandler<E>> {

    private final EventOrder       order;
    private final EventExecutor<E> executor;

    public RegisteredHandler(EventOrder order, EventExecutor<E> executor) {
        this.order = order;
        this.executor = executor;
    }

    public EventOrder getOrder() {
        return order;
    }

    public EventExecutor<E> getExecutor() {
        return executor;
    }

    @Override
    public int compareTo(RegisteredHandler<E> other) {
        return order.compareTo(other.order);
    }
}
