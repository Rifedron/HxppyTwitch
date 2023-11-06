package miau.hxppytwitch.discord;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DiscCommand {
    String name();
    String description();
}
