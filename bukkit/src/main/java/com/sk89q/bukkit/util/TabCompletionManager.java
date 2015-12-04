package com.sk89q.bukkit.util;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedTabCompletion;
import com.sk89q.minecraft.util.commands.TabCompletion;
import com.sk89q.minecraft.util.commands.TabContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TabCompletionManager {
    public Map<Method, Map<String, Method>> tabCompletions;

    public TabCompletionManager() {
        tabCompletions = new HashMap<>();
    }


    public List<String> execute(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results;

        String[] newArgs = new String[args.length + 1];
        newArgs[0] = command.getName().toLowerCase();
        System.arraycopy(args, 0, newArgs, 1, args.length);

        results = executeTab(null, 0, sender, command, alias, newArgs);

        return results;
    }

    public List<String> executeTab(Method parent, int depth, CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        String token = args[depth];
        String part = (depth + 1) < args.length ? args[depth + 1] : "";

        Map<String, Method> tab = tabCompletions.get(parent);
        Method method = tab.get(token.toLowerCase());

        int argsLength = args.length - depth;

        if(method == null)
            return new ArrayList<>();

        if(!checkPermission(sender, method))
            return new ArrayList<>();

        boolean nestedTab = method.isAnnotationPresent(NestedTabCompletion.class) && argsLength > 2;

        if(nestedTab) {
            return executeTab(method, depth + 1, sender, command, alias, args);
        } else if(depth + 2 == args.length){
            TabCompletion tabCompletion = method.getAnnotation(TabCompletion.class);
            if(tabCompletion.useTabOptions()) {
                List<String> options = Arrays.asList(tabCompletion.tabOptions());
                if(method.isAnnotationPresent(NestedTabCompletion.class)) { //Automatically add childern to taboptions
                    Map<String, Method> childern = tabCompletions.get(method);
                    if(childern != null)
                        childern.keySet().stream().filter(c -> !options.contains(c)).forEach(options::add);
                }
                for(String s : options)
                    if(s.startsWith(part)) {
                        results.add(s);
                    }
            } else {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                TabContext tabContext = new TabContext(alias, newArgs);
                Object[] oArgs = new Object[] {tabContext, sender, command};
                try {
                    results = (List<String>) method.invoke(null, oArgs);
                }
                catch (IllegalAccessException e) {}
                catch (InvocationTargetException e) {}
            }
        }
        return results;
    }

    private boolean checkPermission(CommandSender sender, Method method) {
        CommandPermissions permissions = method.getAnnotation(CommandPermissions.class);

        if(permissions == null)
            return true;

        for(String perm : permissions.value())
            if(sender.hasPermission(perm))
                return true;

        return false;
    }
}