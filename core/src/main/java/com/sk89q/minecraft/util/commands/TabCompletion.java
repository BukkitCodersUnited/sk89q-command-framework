package com.sk89q.minecraft.util.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicates a method that will be used as a Tab Completer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TabCompletion {

    /**
     * Indicates name of the command the tab completion is for
     * */
    String token();

    /**
     * Array of options for tab completion of current token
     */
    String[] tabOptions();

    /**
     * Enable / Disable use of static tab completion list for this command
     * Tab completion with this set to true will use tabOptions instead of
     * calling the method.
     */
    boolean useTabOptions() default false;

}