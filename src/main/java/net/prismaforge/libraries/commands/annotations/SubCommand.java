package net.prismaforge.libraries.commands.annotations;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    @NonNull String[] args() default {};

    @NonNull String permission() default "";

    @NonNull String permissionMessage() default "";
}
