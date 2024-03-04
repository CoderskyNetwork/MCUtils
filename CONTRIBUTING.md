# Contributing to MCUtils

Thank you for taking your time to contrubute to MCUtils! Any help is much
appreciated, however, there are some guidelines you can follow to ensure
your PR gets accepted, as well as some conditions.

## License and code authorship

MCUtils is under the MIT license as specified by [LICENSE.md](https://github.com/xDec0de/MCUtils/blob/master/LICENSE.md),
by contributing to MCUtils you agree that your contribution will now be
a part of MCUtils and under the MIT license too. As a contrubutor, you are
allowed to use the javadoc `@author` tag on your methods or classes, however,
you are not allowed to remove an existing author on any tag, you must only add
your name to the existing tag. Please note that, while very, very unlikely,
your name may get removed from an author tag only if:

- Your code gets removed for any reason, this mostly affects removed methods,
this rarely happens as we don't like to remove features.
- Your code gets completely replaced by an update, this is very unlikely,
you will most likely be kept as an author on the tag.

## Requirements

MCUtils is built to have the lowest amount of requirements / dependencies
possible, however, you still need some things:

- A Java 17 or later JDK
- Maven, 3.8.6 has been used to compile MCUtils, however, it might work with
previous versions

## Adding new features

You are more than welcome to add new features to MCUtils, however, please do it
in a way that makes sense, remember that this changes don't only affect you and
your plugins but also every plugin using MCUtils.

You must be able to justify your changes in your PR description, if you are not
able to do so, it is a clear indicator that you are on the wrong track.

## Changes to existing code

Do not remove or rename existing methods / files, api-breaking changes are
generally bad, if you really consider that a method should be removed in the
future, mark it as deprecated. Functionality should also be kept as intact as
possible as some plugins may rely on a very specific behavior. There are two
differnt types of behaviour, expected and actual behaviour. Expected behaviour
is the one dicted by a javadoc comment as this is what developers will use to
know what a method does. Actual behaviour is the real behaviour of the metod,
controlled by the code itself. If expected and actual behaviours differ, this
is considered a either bug or unspecified behaviour depending on if the actual
behaviour is doing something different than it is **expected** to do or if the
expected behaviour is **ambiguous** enough to not include the actual behaviour
respectively. You are allowed to solve bugs, changing unspecified behaviour
should not be done unless the change is considered necessary, a better solution
is actually adding this behaviour to the javadoc comment so it becomes expected
behaviour. Note that any not documented exception being thrown is considered a
bug and not unspecified behaviour even if it technically should be usnpecified
behaviour.

## Minimize the use of NMS and reflection

Reflection is slow and may break in the near future, use it only if there is
no other possible option, the use of NMS and Craftbukkit packages must directly
be avoided, reflection is still preferred over this.

## Testing your changes

Testing must be done before making any PR, [I](https://github.com/xDec0de)
will test them anyways to ensure everything works as expected, but if
everything works beforehand, that really helps saving up some time.
Please remember that MCUtils is expected to work on a wide range of versions
and platforms, if your change is incompatible with any supported version
(1.12 and higher), you must fix it or mention the issue on your PR if you don't
know how to fix it.

You can compile MCUtils as a plugin by running `mvn install`, the jar file will
be in the target folder of your project.

In order to test your modified version of MCUtils, just add it to your plugins
folder and make sure that your test plugin (The one that will use your version
of MCUtils) is not shading MCUtils by using `<scope>provided</scope>` on
MCUtil's dependency on your `pom.xml`, just like you do with, for example,
Spigot's dependency.
