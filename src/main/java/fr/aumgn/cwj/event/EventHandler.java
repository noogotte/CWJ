package fr.aumgn.cwj.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    EventPhase phase() default EventPhase.HANDLE;

    EventOrder order() default EventOrder.DEFAULT;
}
