package miau.hxppytwitch.discord;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(HasOptions.class)
public @interface Option {
    int optionType() default 3;
    String name();
    String description();
    boolean isRequired() default true;
    
}
