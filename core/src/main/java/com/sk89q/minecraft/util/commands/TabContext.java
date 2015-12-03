package com.sk89q.minecraft.util.commands;

public class TabContext {
    String alias;
    String[] args;

    public TabContext(String alias, String[] args) {
        this.alias = alias;
        this.args = args;
    }

    public String getAlias() {
        return alias;
    }

    public String getArg(int index) throws IndexOutOfBoundsException {
        if(index >= 0 && index < args.length)
            return args[index];
        else
            throw new IndexOutOfBoundsException();
    }

    public String[] getArgs() {
        return args;
    }
}
