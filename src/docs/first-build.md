# Initial Build

The first thing that is going to happen when you try to build the project is that it is going to fail:

```
gradled build

FAILURE: Build failed with an exception.

* What went wrong:
A problem occurred configuring root project 'gradle-plugin-java'.
> Could not resolve all dependencies for configuration ':classpath'.
   > Could not find synapticloop:gradle-plugin-java:1.0.0.
     Searched in the following locations:
         file:/Users/synapticloop/.m2/repository/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.pom
         file:/Users/synapticloop/.m2/repository/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.jar
         https://plugins.gradle.org/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.pom
         https://plugins.gradle.org/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.jar
         https://plugins.gradle.org/m2/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.pom
         https://plugins.gradle.org/m2/synapticloop/gradle-plugin-java/1.0.0/gradle-plugin-java-1.0.0.jar
     Required by:
         :gradle-plugin-java:unspecified

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.

BUILD FAILED

Total time: 2.429 secs
```


This is due to the fact that it cannot find the plugin, since you haven't built the plugin yet, so it is expecting the dependency which hasn't been built, to exist before it can build the extension - kind of a catch-22 situation.

you need to do this:

```
gradle -b build-initial.gradle build pTML
```

the `pTML` above is a short form of `publishToMavenLocal`, so you could also have done:

```
gradle -b build-initial.gradle build publishToMavenLocal
```

This will publish the plugin to maven local, which means that you can now do a:

```
gradle build
```

# The Source Files


# Adding in the extension

