package fr.aumgn.cwj.event;

public interface EventExecutor<E extends Event<E>> {

    void execute(E event);
}
