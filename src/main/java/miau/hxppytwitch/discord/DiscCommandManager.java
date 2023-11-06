package miau.hxppytwitch.discord;

import com.google.common.collect.Lists;
import miau.hxppytwitch.HTPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DiscCommandManager extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        Method[] methods = DiscCommands.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DiscCommand.class)) {
                DiscCommand command = method.getAnnotation(DiscCommand.class);
                if (command.name().equals(commandName)) {
                    try {
                        method.setAccessible(true);
                        method.invoke(new DiscCommands(), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
        }
        event.getInteraction().reply("Команда не найдена")
                .queue();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();
        guild.updateCommands().queue();
        Method[] methods = DiscCommands.class.getDeclaredMethods();
        for (Method method: methods) {
            if (method.isAnnotationPresent(DiscCommand.class)) {
                DiscCommand command = method.getAnnotation(DiscCommand.class);
                CommandCreateAction createAction = guild.upsertCommand(command.name(), command.description());
                if (method.isAnnotationPresent(HasOptions.class)) {
                    Option[] options = method.getAnnotation(HasOptions.class).value();
                    for (Option option : options) {
                        createAction.addOption(OptionType.fromKey(option.optionType()),
                                option.name(), option.description(), option.isRequired());
                    }
                }
                createAction.queue(System.out::println);
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getInteraction().getButton().getId();
        Method[] methods =Buttons.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Button.class)) {
                Button button = method.getAnnotation(Button.class);
                if (button.id().equals(buttonId)) {
                    try {
                        method.setAccessible(true);
                        method.invoke(new Buttons(), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
        }
    }
}
