# Initial Build

The first thing that is going to happen when you try to build the project is that it is going to fail:

```
gradle build


FAILURE: Build failed with an exception.

* What went wrong:
A problem occurred configuring root project 'javaSample'.
> Could not resolve all dependencies for configuration ':classpath'.
   > Could not find synapticloop:javaSample:1.0.0.
     Searched in the following locations:
         file:/Users/synapticloop/.m2/repository/synapticloop/javaSample/1.0.0/javaSample-1.0.0.pom
         file:/Users/synapticloop/.m2/repository/synapticloop/javaSample/1.0.0/javaSample-1.0.0.jar
         https://plugins.gradle.org/synapticloop/javaSample/1.0.0/javaSample-1.0.0.pom
         https://plugins.gradle.org/synapticloop/javaSample/1.0.0/javaSample-1.0.0.jar
         https://plugins.gradle.org/m2/synapticloop/javaSample/1.0.0/javaSample-1.0.0.pom
         https://plugins.gradle.org/m2/synapticloop/javaSample/1.0.0/javaSample-1.0.0.jar
     Required by:
         :javaSample:unspecified

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

Here is a list of the within this project

```
├── .gitignore  // this is git ignore file - generated from https://www.gitignore.io/api/gradle,eclipse,java
├── build-initial.gradle  // this is the initial build file - invoked by gradle -b build-initial build pTML
├── build.gradle  // the main build.gradle file
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew  // gradle invoker script
├── gradlew.bat  // windows gradle invoker script
├── settings.gradle // settings for the gradle build
└── src
    ├── docs
    │   └── first-build.md  // documentation
    ├── main
    │   ├── java
    │   │   └── synapticloop
    │   │       └── gradle
    │   │           └── plugin
    │   │               ├── JavaSampleGoodbyeTask.java  // the goodbye task
    │   │               ├── JavaSampleHelloTask.java // the hello task
    │   │               ├── JavaSamplePlugin.java // the plugin registration
    │   │               └── JavaSamplePluginExtension.java // the extension point for configuration
    │   └── resources
    │       └── META-INF
    │           └── gradle-plugins
    │               └── synapticloop.javaSample.properties // the properties file to register the plugin
    └── test
        └── java
```

# Adding in the extension

The extension allows the user of your plugin to set or over-ride defaults.  You
should always set sensible defaults for all options.

# Updating Versions - Catch-22

There is a little bit of back and forth with the plugin development.  For 
example if you wish to update the version, you will need to build the version 
first, then update the registration, then run the plugin.  E.g updating to 
version 1.0.1.

## Step 1 - update the build version number

in the `build.gradle` file - the lines

```
// textual information for this project
group = 'synapticloop'
archivesBaseName = 'javaSample'
description = """An example gradle plugin written in Java"""

version = '1.0.0'
```

change the `version = '1.0.0'` to `version = '1.0.1'`

## Step 2 - build and publish the new version locally

```
gradle build pTML
```

## Step 3 - update the included plugin version

then in the `build.gradle` file:

```
buildscript {
	repositories {
		mavenLocal()
		maven {
			url "https://plugins.gradle.org/"
		}
	}

	dependencies {
		classpath 'synapticloop:javaSample:1.0.0'
	}
}
```

change the line `classpath 'synapticloop:javaSample:1.0.0'` to `classpath 'synapticloop:javaSample:1.0.1'` 
and you may then invoke the task with the updated version number.

## Step 4 - invoke the new task

```
gradle javaSampleHello
```

# WARNING ABOUT DAEMON MODE

Since gradle caches the plugin - it is best to **NOT** use `--daemon` in your 
build file if you do not update the plugin version number.

## Gradle 3.0+

From Gradle 3.0 onwards, `--daemon` is defaulted to be on all of the time, you 
need to disable the daemon (see [https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:disabling_the_daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:disabling_the_daemon)) for instructions on how to disable it.

In a nutshell, there are two ways to disable the daemon:

 1. Via environment variables: add the flag `-Dorg.gradle.daemon=false` to the `GRADLE_OPTS` environment variable
 1. Via properties file: add `org.gradle.daemon=false` to the `«GRADLE_USER_HOME»/gradle.properties` file


# Publishing the Plugin

## Step 1 - Register your plugin group

Go to [https://login.gradle.org/user/register](https://login.gradle.org/user/register) 
and fill in the details:

> **NOTE**: Your username will become the group - i.e. if your username is `synapticloop` then your plugins will be registered under the `synapticloop` group and will reside here [https://plugins.gradle.org/u/synapticloop](https://plugins.gradle.org/u/synapticloop).

## Step 2 - Get your API key

Click on the `API Keys` link in the tabbed section, which will list your api keys:

```
gradle.publish.key=SOMETHING
gradle.publish.secret=SOMETHING_SECRET
```

Copy them to your HOME_DIR/.gradle/gradle.properties (~/.gradle/gradle.properties) file:


## Step 3 - install the gradle plugin publishing plugin

In your `build.gradle` file in the `plugins` section, make sure you have included 
the `com.gradle.plugin-publish` plugin:

```
// list all of the plugins for this project
plugins {
	...

	id 'com.gradle.plugin-publish' version '0.9.3'

	...
}
```

## Step 4 - Set up the plugin publishing task

Include all of the following in your `build.gradle` file


```
/**
 * Below is everything that you need to publish your plugin
 */

// The fatJar tasks assembles all of the output files (compiled java, properties 
// etc.) into a single jar
task fatJar(type: Jar) {
	classifier = 'all'

	from(sourceSets.main.output) { include "**" }
}

// we want to ensure that the fatJar task is run
build.finalizedBy(project.tasks.fatJar)
publishPlugins.finalizedBy(project.tasks.fatJar)

// links to javadoc - the javadoc task is needed for the maven publication, not
// for the publishing of plugins
def javaApiUrl = 'http://docs.oracle.com/javase/1.7.0/docs/api/'
def groovyApiUrl = 'http://groovy.codehaus.org/gapi/'

tasks.withType(Javadoc) {
	options.links(javaApiUrl, groovyApiUrl)
}

task javadocJar(type: Jar, dependsOn: javadoc) {
	classifier = 'javadoc'
	from 'build/docs/javadoc'
}

task sourcesJar(type: Jar) {
	from sourceSets.main.allSource
	classifier = 'sources'
}

publishing {
	publications {
		Synapticloop(MavenPublication) {
			from components.java
			artifact sourcesJar
			artifact javadocJar

			groupId group
			artifactId archivesBaseName

			pom.withXml {
				configurations.compile.resolvedConfiguration.firstLevelModuleDependencies.each { dep ->
					asNode().dependencies[0].dependency.find {
						it.artifactId[0].text() == dep.moduleName &&
								it.groupId[0].text() == dep.moduleGroup
					}.scope[0].value = 'compile'
				}
			}
		}
	}
}

// Now it is time to publish the plugin
pluginBundle {
	website = 'https://github.com/synapticloopltd/gradle-plugin-java-sample'
	vcsUrl = 'https://github.com/synapticloopltd/gradle-plugin-java-sample'
	description = 'A sample gradle plugin built with java'
	tags = [ 'sample' ]

	plugins {
		gradlePluginJava {
			id = 'synapticloop.javaSample'
			displayName = 'Synapticloop sample Gradle plugin in Java'
		}
	}
}
```

You can now publish your plugin with a simple:

```
gradle publishPlugin
```

or more concisely:

```
gradle pP
```

# Creating a new plugin

## Required Information

 - plugin group - referred to as `«PLUGIN_GROUP»`
 - plugin description = referred to as `«PLUGIN_DESCRIPTION»`
 - plugin name - referred to as `«PLUGIN_NAME»`
 - plugin package - referred to as `«PLUGIN.PACKAGE»`
 - plugin path - referred to as `«PLUGIN_PATH»` - this should match the plugin package as per java conventions


## Step 1 - create the `.properties` file

Create a file located at:

```
src/main/resources/META-INF/«PLUGIN_GROUP».«PLUGIN_NAME».properties
```

Enter the following details:

```
implementation-class=«PLUGIN_GROUP».«PLUGIN_NAME»Plugin
```

## Step 2 - create the `Plugin.java` file

Create a file located at:

```
src/main/java/«PLUGIN_PATH»/«PLUGIN_NAME»Plugin.java
```

This **MUST** match the `implementation-class` listed as per Step 1

The file should contain the following

```
package «PLUGIN.PACKAGE»;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class «PLUGIN_NAME»Plugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getExtensions().create("«PLUGIN_NAME»", «PLUGIN_NAME»PluginExtension.class);

		project.getTasks().create("«PLUGIN_TASK_NAME»", «PLUGIN_NAME»Task.class);
	}
}

```

## Step 3 - create the `PluginExtension.java` file

Create a file located at:

```
src/main/java/«PLUGIN_PATH»/«PLUGIN_NAME»PluginExtension.java
```

The file should contain the following:

```
package «PLUGIN.PACKAGE»;


public class «PLUGIN_NAME»PluginExtension {
	// here you can put your member variables

	// don't forget to create getters/setters for all of the member variables
}
```

## Step 4 - create the `Task.java` file

Create a file located at:

```
src/main/java/«PLUGIN_PATH»/«PLUGIN_NAME»Task.java
```

The file should contain the following:

```
package «PLUGIN.PACKAGE»;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

public class «PLUGIN_NAME»Task extends DefaultTask {
	private Logger logger;
	private «PLUGIN_NAME»PluginExtension extension;

	public JavaSampleHelloTask() {
		setGroup("«PLUGIN_GROUP»");
		setDescription("«PLUGIN_DESCRIPTION»");

		this.logger = getProject().getLogger();
	}

	@TaskAction
	public void generate() {
		extension = getProject().getExtensions().findByType(«PLUGIN_NAME»PluginExtension.class);

		if (extension == null) {
			extension = new «PLUGIN_NAME»Extension();
		}

		logger.info("Logging something, this will only be seen if the gradle task is invoked with --info");
	}
}
```

## Step 5 - create the `build-initial.gradle` file

Use the `./build-initial.gradle` file as a starting point, you will need to 
update the following values:

```
// textual information for this project
group = '«PLUGIN_ARTEFACT_BASE»'
archivesBaseName = '«PLUGIN_NAME»'
description = """«PLUGIN_DESCRIPTION»"""

version = '1.0.0'
```

## Step 6 - create the `build.gradle` file

Use the `./build.gradle` file as a starting point, you will need to 
update the values in the file as per step 5 above.


