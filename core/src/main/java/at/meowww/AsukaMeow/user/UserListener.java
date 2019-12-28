package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.dialog.AlignOption;
import at.meowww.AsukaMeow.dialog.Dialog;
import at.meowww.AsukaMeow.dialog.line.LineBreak;
import at.meowww.AsukaMeow.dialog.line.LineBuilder;
import at.meowww.AsukaMeow.dialog.page.PageOption;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private UserManager manager;

    Dialog dialog;

    public UserListener (UserManager manager) {
        this.manager = manager;
        try {
            dialog = new Dialog(
                    PageOption.build(new LineBuilder("[選項A]").bold(true).color(ChatColor.DARK_GREEN).click(player -> {
                        player.setDisplayName("AAAAA");
                    }).hover().create()),
                    PageOption.build(new BaseComponent[] {
                            new LineBuilder("這是一些文本哈囉你好嗎").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                            new LineBuilder("我想要魔獸世界的故事").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                            new LineBuilder("罷拖可以來一點嗎？").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                            new LineBuilder("雞排好吃到可以直接成佛").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                            new LineBuilder("還有綠茶半糖去冰").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                            new LineBuilder("這是測試行數用的拉雞行啦").color(ChatColor.BLACK).create(),
                            LineBreak.create(),
                    }, new BaseComponent[] {
                            new LineBuilder("[嫉妒]").bold(true).color(ChatColor.BLACK).click(player -> {
                                player.setHealth(0.0D);
                            }).append("[我也要吃]").bold(true).color(ChatColor.BLACK).click(player -> {
                                player.setHealth(0.5D);
                            }).create(AlignOption.SPLIT),
                            new LineBuilder("[吃吃吃]").bold(true).color(ChatColor.BLACK).click(player -> {
                                player.setAllowFlight(true);
                            }).append("[巴哈都是刁民]").bold(true).color(ChatColor.BLACK).click(player -> {
                                player.setAllowFlight(false);
                            }).create(AlignOption.SPLIT),
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        User onlineUser = manager.findUser(uuid);
        if (onlineUser == null) {
            onlineUser = User.newUser(player);
            manager.updateUser(onlineUser);
        }
        onlineUser.online(player);
        manager.onlineUser.put(uuid, onlineUser);

        player.setDisplayName(onlineUser.getDisplayName());
        player.setPlayerListName(onlineUser.getDisplayName());

        AsukaMeow.INSTANCE
                .getNMSManager()
                .getDialogFactory()
                .openDialog(player, dialog);
    }

    @EventHandler
    public void onPlayerLeave (PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        User offlineUser = manager.onlineUser.remove(uuid);
        offlineUser.offline(event.getPlayer());
        manager.updateUser(offlineUser);
    }

}
