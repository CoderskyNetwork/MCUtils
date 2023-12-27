# About MCUtils
MCUtils is a free to use open-source plugin API to help you develop your own Spigot plugins, designed to be shaded under your plugin so you don't have to worry about new versions releasing that administrators may need to download, they don't need to download anything! Once you compile your plugin, the behavior of MCUtils won't change, unless you explicitly change to another version of it, of course.

![MCUtils banner](https://user-images.githubusercontent.com/63256529/192231939-b606878c-4436-4d74-9cb6-b78ce6850240.jpg)
<div align=center>
<a href="https://www.codefactor.io/repository/github/xdec0de/mcutils"><img src="https://www.codefactor.io/repository/github/xdec0de/mcutils/badge"</img></a>
<a href="https://app.codacy.com/gh/xDec0de/MCUtils/"><img src="https://app.codacy.com/project/badge/Grade/2d121db7e16749f49cdc3cdd897da9fe"></img></a>
<a href="https://github.com/xDec0de/MCUtils/actions/workflows/build.yml"><img src="https://img.shields.io/github/actions/workflow/status/xDec0de/MCUtils/build.yml?branch=master"</img></a>
</div>

## The reliability of MCUtils
Well, you can never be sure that a program is 100% reliable on any environment with an almost infinite amount of conditions that Minecraft servers tend to have, of course, bugs may appear sooner or later, it's part of the development process of any program. However, MCUtils is used by my own plugins which are tested quite a lot, in fact, I even use it as a part of the core of my own network, so you don't have to worry about the project being abandoned any time soon either. Most bugs / performance issues will be solved before a version is even released or they get reported. But, if you do find a bug, critical or not, feel free to [contribute](https://github.com/xDec0de/MCUtils/blob/master/CONTRIBUTING.md) / [create an issue](https://github.com/xDec0de/MCUtils/issues/new/choose) to fix it!

## The current state of MCUtils, not ready for production yet
One thing that is important to note is that MCUtils is currently on the early stages of development, features may *(And will)* change, methods may be renamed or removed, and of course issues are expected to appear. This is normal right now! **API-breaking** changes will happen with a lower and lower frequency as we approach the first stable release, these changes are necessary right now that we can change most of the code without worrying about a ton of plugins breaking, making sure that all features are robust and easy to use in the better way possible. A great example of this is how the whole command API changed in order to add a more minimalistic and simple approach to it, this **WON'T** happen once the first stable build of MCUtils gets released, so **don't panic!**

## Why should I use MCUtils? (Documentation in progress)
As previously mentioned, MCUtils is designed to make your plugin development process easier and faster, but, what does it actually offer? Well... Here is the current list of features.
-   Compatible with both Spigot and PaperMC

-   General file registration
    - [Reload and update](https://mcutils.codersky.net/file-types/messagesfile) all the files registered to your plugin
    - [FileHolder](https://mcutils.codersky.net/file-types#fileholder-and-fileupdater) interface for any custom file type that you may need to register
    - [FileUpdater](https://mcutils.codersky.net/file-types#fileholder-and-fileupdater) interface if your FileHolder can be updated
-   Yaml file management
    - [YmlFile](https://mcutils.codersky.net/file-types/ymlfile) for basic yaml files with reload support
    - [PluginFile](https://mcutils.codersky.net/file-types/pluginfile) for plugin resource files that can be updated
    - [MessagesFile](https://mcutils.codersky.net/file-types/messagesfile) to send custom messages easily

-   Message patterns
    - [Color patterns](https://mcutils.codersky.net/chat-features/color-patterns)
    - [Target patterns](https://mcutils.codersky.net/chat-features/target-patterns)
    - [Event patterns](https://mcutils.codersky.net/chat-features/event-patterns)

-   The MCCommand class
    - Register commands with or without defining them on your plugin.yml file
    - Get and convert command arguments in one line, without IndexOutOfBounds exceptions
    - Add sub-commands without doing all the boring logic of it yourself
    - Restrict commands to be player or console only directly from the constructor
    - Create your own MCCommand class with custom conditions

-   Builders that are easy to use
    - [ItemBuilder](https://mcutils.codersky.net/items-and-inventories/itembuilder)
    - SkullBuilder
    - InventoryBuilder

-   GUI handling
    - GUIHandler
    - GUI and ActionGUI

-   Reflection utilities

-   Regions
    - [2D Regions](https://mcutils.codersky.net/regions/2d-regions)
    - [3D Regions](https://mcutils.codersky.net/regions/3d-regions)

-   General utilities
    - MCStrings
    - [Replacers](https://mcutils.codersky.net/chat-features/replacers)
    - MCNumbers
    - MCLists

-   Version checkers
    - [For the server](https://mcutils.codersky.net/getting-started/checking-server-version)
    - For your plugin

-   World generation
    - VoidGenerator
    - SingleBiomeProvider
