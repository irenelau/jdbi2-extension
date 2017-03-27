package com.callingtree.opensource.jdbi.extension.annotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used to specify the IN conditions
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Equals {

    String alias();

    @NotNull
    String columnName();
    
}
