# Contributing to MCUtils
Thank you for taking your time to contrubute to MCUtils! Any help is much appreciated, however, there are some guidelines you can follow to ensure your PR gets accepted.

# Requirements
MCUtils is built to have the lowest amount of requirements / dependencies possible, however, you still need some things:

- A Java 17 or later JDK
- Maven, 3.8.6 has been used to compile MCUtils, however, it might work with previous versions

# Adding new features
You are more than welcome to add new features to MCUtils, however, please do it in a way that makes sense, remember that this changes don't only affect you and your plugins but also every plugin using MCUtils.

You must be able to justify your changes in your PR description, if you are not able to do so, it is a clear indicator that you are on the wrong track.

# Changes to existing code
Do not remove or rename existing methods / files, api-breaking changes are generally bad, if you really consider that a method should be removed in the future, mark it as deprecated.

# Testing your changes
Testing must be done before making any PR, I will test them anyways to ensure everything works as expected, but if everything works beforehand, that really helps saving up some time.

You can compile the plugin by running `mvn install`, the jar file will be in the target folder of your project.

In order to test the compiled plugin, just add it to your project classpath, this step depends on the IDE you are using, then you can create a local spigot server and add both your test plugin and your new version of MCUtils to its plugins folder.
