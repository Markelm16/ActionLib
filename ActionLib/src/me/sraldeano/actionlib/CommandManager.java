/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.sraldeano.actionlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.sraldeano.actionlib.util.TextUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 *
 * @author markelm
 */
public class CommandManager implements CommandExecutor{

    private static String[] help = {
        "&6&m------------&r&6[Commands]&6&m-------------",
        "&e/actionlib testaction (Action) [Args]",
        "&cPerform an Action on yourself",
        "&e/actionlib about",
        "&cGet basic plugin info",
        "&e/actionlib addons",
        "&cGet a list of loaded addons",
        "&e/actionlib reload",
        "&cReload the configuration",
        "&e/actionlib help",
        "&cShows this help message",
        "&6&m------------&r&6[Commands]&6&m-------------"
    };

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String main, String[] args) {
        if (args.length == 0) {
            cs.sendMessage(TextUtil.colored(help));
            return true;
        }
        if (cs instanceof  Player) {
            if (!cs.hasPermission("actionlib.admin")) {
                cs.sendMessage(TextUtil.colored("&cYou don't have permission to execute this command"));
                return true;
            }
        }
        switch (args[0].toLowerCase()) {
            case "testaction" : {
                if (!(cs instanceof Player)) {
                    cs.sendMessage("This command cannot be executed from the console.");
                }
                ActionManager actionManager = new ActionManager((Player) cs).setAction(args[1]);
                ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
                argsList.remove(0);
                argsList.remove(0);
                Player p = (Player) cs;
                String setting = "";
                if (argsList.size() != 0) {
                    for (String arrayPart : argsList) {
                        setting = setting + arrayPart + " ";
                    }
                    setting.substring(0, setting.length() - 1);
                    actionManager.setSettings(setting);
                }
                actionManager.execute();
                break;
            }
            case "about" : {
                String[] about = {"&6&m-----------------&r&6[]&6&m------------------",
                "&6Created by: &3Sr_Aldeano",
                "&6Version: &3" + ActionLib.plugin.getDescription().getVersion(),
                "&6GitHub: &3github.com/Markelm16/ActionLib",
                "&6Wiki: &3https://www.spigotmc.org/wiki/actionlib",
                "&6&m-----------------&r&6[]&6&m------------------"
                };
                cs.sendMessage(TextUtil.colored(about));
                break;
            }
            case "reload" : {
                ActionLib.plugin.reloadConfig();
                cs.sendMessage(ChatColor.GREEN + "Config reloaded.");
                break;
            }
            case "addons" : {
                List<Action> actionList = ActionLib.getAddonActions();
                if (actionList.size() < 1) {
                    cs.sendMessage(ChatColor.YELLOW + "Currently there are not addons loaded.");
                    return true;
                }
                cs.sendMessage(ChatColor.GREEN + "Currently loaded addons:");
                for (Action addon : ActionLib.getAddonActions()) {
                    cs.sendMessage(ChatColor.AQUA + "- " + addon.getName());
                }
                break;
            }
            case "help" : {
                cs.sendMessage(TextUtil.colored(help));
                break;
            }
            default:
                cs.sendMessage(TextUtil.colored(help));
                break;
        }
        return true;
    }
}
