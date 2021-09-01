package com.redblock6.mccore.bot;

import me.invisibledrax.alliances.Main;
import me.invisibledrax.alliances.truces.Nations;
import me.invisibledrax.alliances.truces.NationReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Arrays;

public class BotMain extends ListenerAdapter {
    public Main pl;
    public JDA bot;
    public static MessageChannel eventsChannel;

    public BotMain(Main main) {
        this.pl = main;
        initializeBot();
        bot.addEventListener(this);
        eventsChannel = bot.getTextChannelById(pl.getConfig().getString("bot-channel-id"));
    }

    public void sendNationsAnnouncement(String owner, String name, NationReason reason, String... args) {
        MessageChannel channel = eventsChannel;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(name);
        if (reason.equals(NationReason.CREATED)) {
            eb.setColor(Color.RED);
            eb.setDescription(owner + " created a new nation called the " + name);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
        } else if (reason.equals(NationReason.DISBANDED)) {
            eb.setColor(Color.RED);
            eb.setDescription(owner + " disbanded the " + name);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
        } else if (reason.equals(NationReason.MEMBER_ADDED)) {
            eb.setColor(Color.RED);
            eb.setDescription(args[0] + " joined the " + name);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
            eb.addField("Members", Arrays.toString(Nations.getTruce(name).getMemberNames().toArray()), false);
        } else if (reason.equals(NationReason.MEMBER_KICKED)) {
            eb.setColor(Color.RED);
            eb.setDescription(args[0] + " was kicked from the " + name);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
            eb.addField("Members", Arrays.toString(Nations.getTruce(name).getMemberNames().toArray()), false);
        } else if (reason.equals(NationReason.MEMBER_PROMOTED)) {
            eb.setColor(Color.RED);
            eb.setDescription(args[0] + " was promoted from " + args[1] + " to " + args[3] + " in the " + name);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
            eb.addField("Members", Arrays.toString(Nations.getTruce(name).getMemberNames().toArray()), false);
        } else if (reason.equals(NationReason.CHANGED_NAME)) {
            eb.setTitle(args[0] + " -> " + args[1]);
            eb.setColor(Color.RED);
            eb.setDescription("The name of " + args[0] + " was changed to " + args[1]);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
        } else if (reason.equals(NationReason.CHANGED_DESC)) {
            eb.setColor(Color.RED);
            eb.setDescription("The desc of " + name + " was changed to " + args[0]);
            eb.addField("Name", name, false);
            eb.addField("Leader", owner, false);
            eb.addField("Members", Arrays.toString(Nations.getTruce(name).getMemberNames().toArray()), false);
        }
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    public void initializeBot() {
        JDABuilder builder = JDABuilder.createDefault(pl.getConfig().getString("bot-token"));
        builder.setActivity(Activity.watching(" over the RedSMP"));
        try {
            bot = builder.build();
            bot.awaitReady();

        } catch (LoginException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMessage(User user, String content) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }

}
