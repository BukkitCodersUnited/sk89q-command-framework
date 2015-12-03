sk89q-command-framework
=======================

sk89q-command-framework is the command framework from sk89q's WorldEdit. It has been factored out so it may be used in other projects without having to include WorldEdit as a dependency.

Warning
--------

This is a custom build of the command framework implementing a tab completion option into the framework.

Compiling
---------

You need to have Maven installed (http://maven.apache.org). Once installed, simply run:

    mvn clean install

Maven will automatically download dependencies for you. Note: For that to work, be sure to add Maven to your "PATH".

Contributing
------------

Your submissions have to be licensed under the GNU General Public License v3.


Tab Completion
---------------

**token** - Current fully completed token of the command  
**tabOptions** - Array of strings to be used as a list for tab completion if useTabOptions is true for this token  
**useTabOptions** - If true tabOptions will be used in tab completion, If false the method will be called  
**Method definition** - ```public static void test(TabContext context, CommandSender sender, Command command)``` 
  
**@NestedTabCompletion** Allows for nested tab completion of commands, works like **@NestedCommand**  
