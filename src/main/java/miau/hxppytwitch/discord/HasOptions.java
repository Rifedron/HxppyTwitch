package miau.hxppytwitch.discord;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasOptions {
    Option[] value();
}
