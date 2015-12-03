package com.sk89q.bukkit.util;
;
import com.sk89q.minecraft.util.commands.NestedTabCompletion;
import com.sk89q.minecraft.util.commands.TabCompletion;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class TabCompletionRegistration {
    TabCompletionManager manager;
    CommandMap map;
    Plugin owning;

    public TabCompletionRegistration(TabCompletionManager manager, CommandRegistration commandRegistration, Plugin owning) {
        this.manager = manager;
        this.map = commandRegistration.getCommandMap();
        this.owning = owning;
    }

    public void registerTabCompletions(Class clazz) {
        registerTabCompletions(clazz, null);
    }

    private void registerTabCompletions(Class clazz, Method parent) {
        Map<String, Method> tabMap;

        if(manager.tabCompletions.containsKey(parent)) {
            tabMap = manager.tabCompletions.get(parent);
        } else {
            tabMap = new HashMap<>();
            manager.tabCompletions.put(parent, tabMap);
        }

        for(Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(TabCompletion.class))
                continue;
            if (!Modifier.isStatic(method.getModifiers()))
                continue;

            String token = method.getAnnotation(TabCompletion.class).token();
            tabMap.put(token, method);

            if (parent == null) {//base command, register tab completer
                ((DynamicPluginCommand) map.getCommand(token)).setTabCompleter(owning);
            }

            if(method.isAnnotationPresent(NestedTabCompletion.class)) {
                NestedTabCompletion nestedTabs = method.getAnnotation(NestedTabCompletion.class);
                for(Class<?> cla : nestedTabs.value())
                    registerTabCompletions(cla, method);
            }
        }
    }
}