package net.prismaforge.libraries.commands.annotations;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    @NonNull String name();

    @NonNull String description() default "";

    @NonNull String usage() default "";

    @NonNull String[] aliases() default "";

    boolean tabComplete() default true;
}
