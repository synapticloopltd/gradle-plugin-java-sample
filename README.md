 <a name="#documentr_top"></a>

> **This project requires JVM version of at least 1.7**






<a name="documentr_heading_0"></a>

# Table of Contents <sup><sup>[top](#documentr_top)</sup></sup>



 - [Table of Contents](#documentr_heading_0)
 - [javaSample](#documentr_heading_1)
 - [Initial Build](#documentr_heading_2)
 - [The Source Files](#documentr_heading_3)
 - [Adding in the extension](#documentr_heading_4)
 - [Updating Versions - Catch-22](#documentr_heading_5)
   - [Step 1 - update the build version number](#documentr_heading_6)
   - [Step 2 - build and publish the new version locally](#documentr_heading_7)
   - [Step 3 - update the included plugin version](#documentr_heading_8)
   - [Step 4 - invoke the new task](#documentr_heading_9)
 - [WARNING ABOUT DAEMON MODE](#documentr_heading_10)
   - [Gradle 3.0+](#documentr_heading_11)
 - [Publishing the Plugin](#documentr_heading_12)
   - [Step 1 - Register your plugin group](#documentr_heading_13)
   - [Step 2 - Get your API key](#documentr_heading_14)
   - [Step 3 - install the gradle plugin publishing plugin](#documentr_heading_15)
   - [Step 4 - Set up the plugin publishing task](#documentr_heading_16)
 - [Creating a new plugin](#documentr_heading_17)
   - [Required Information](#documentr_heading_18)
   - [Step 1 - create the .properties file](#documentr_heading_19)
 - [Building the Package](#documentr_heading_20)
   - [*NIX/Mac OS X](#documentr_heading_21)
   - [Windows](#documentr_heading_22)
 - [Running the Tests](#documentr_heading_23)
   - [*NIX/Mac OS X](#documentr_heading_24)
   - [Windows](#documentr_heading_25)
   - [Dependencies - Gradle](#documentr_heading_26)
   - [Dependencies - Maven](#documentr_heading_27)
   - [Dependencies - Downloads](#documentr_heading_28)






<a name="documentr_heading_1"></a>

# javaSample <sup><sup>[top](#documentr_top)</sup></sup>



> An example gradle plugin written in Java




<a name="documentr_heading_2"></a>

# Initial Build <sup><sup>[top](#documentr_top)</sup></sup>

The first thing that is going to happen when you try to build the project is that it is going to fail:



```
gradled build


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





<a name="documentr_heading_3"></a>

# The Source Files <sup><sup>[top](#documentr_top)</sup></sup>

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





<a name="documentr_heading_4"></a>

# Adding in the extension <sup><sup>[top](#documentr_top)</sup></sup>

The extension allows the user of your plugin to set or over-ride defaults.  You
should always set sensible defaults for all options.



<a name="documentr_heading_5"></a>

# Updating Versions - Catch-22 <sup><sup>[top](#documentr_top)</sup></sup>

There is a little bit of back and forth with the plugin development.  For 
example if you wish to update the version, you will need to build the version 
first, then update the registration, then run the plugin.  E.g updating to 
version 1.0.1.



<a name="documentr_heading_6"></a>

## Step 1 - update the build version number <sup><sup>[top](#documentr_top)</sup></sup>

in the `build.gradle` file - the lines



```
// textual information for this project
group = 'synapticloop'
archivesBaseName = 'javaSample'
description = """An example gradle plugin written in Java"""

version = '1.0.0'
```



change the `version = '1.0.0'` to `version = '1.0.1'`



<a name="documentr_heading_7"></a>

## Step 2 - build and publish the new version locally <sup><sup>[top](#documentr_top)</sup></sup>



```
gradle build pTML
```





<a name="documentr_heading_8"></a>

## Step 3 - update the included plugin version <sup><sup>[top](#documentr_top)</sup></sup>

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



<a name="documentr_heading_9"></a>

## Step 4 - invoke the new task <sup><sup>[top](#documentr_top)</sup></sup>



```
gradle javaSampleHello
```





<a name="documentr_heading_10"></a>

# WARNING ABOUT DAEMON MODE <sup><sup>[top](#documentr_top)</sup></sup>

Since gradle caches the plugin - it is best to **NOT** use `--daemon` in your 
build file if you do not update the plugin version number.



<a name="documentr_heading_11"></a>

## Gradle 3.0+ <sup><sup>[top](#documentr_top)</sup></sup>

From Gradle 3.0 onwards, `--daemon` is defaulted to be on all of the time, you 
need to disable the daemon (see [https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:disabling_the_daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:disabling_the_daemon)) for instructions on how to disable it.

In a nutshell, there are two ways to disable the daemon:

 1. Via environment variables: add the flag `-Dorg.gradle.daemon=false` to the `GRADLE_OPTS` environment variable
 1. Via properties file: add `org.gradle.daemon=false` to the `«GRADLE_USER_HOME»/gradle.properties` file




<a name="documentr_heading_12"></a>

# Publishing the Plugin <sup><sup>[top](#documentr_top)</sup></sup>



<a name="documentr_heading_13"></a>

## Step 1 - Register your plugin group <sup><sup>[top](#documentr_top)</sup></sup>

Go to [https://login.gradle.org/user/register](https://login.gradle.org/user/register) 
and fill in the details:

> **NOTE**: Your username will become the group - i.e. if your username is `synapticloop` then your plugins will be registered under the `synapticloop` group and will reside here [https://plugins.gradle.org/u/synapticloop](https://plugins.gradle.org/u/synapticloop).



<a name="documentr_heading_14"></a>

## Step 2 - Get your API key <sup><sup>[top](#documentr_top)</sup></sup>

Click on the `API Keys` link in the tabbed section, which will list your api keys:



```
gradle.publish.key=SOMETHING
gradle.publish.secret=SOMETHING_SECRET
```



Copy them to your HOME_DIR/.gradle/gradle.properties (~/.gradle/gradle.properties) file:




<a name="documentr_heading_15"></a>

## Step 3 - install the gradle plugin publishing plugin <sup><sup>[top](#documentr_top)</sup></sup>

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





<a name="documentr_heading_16"></a>

## Step 4 - Set up the plugin publishing task <sup><sup>[top](#documentr_top)</sup></sup>

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





<a name="documentr_heading_17"></a>

# Creating a new plugin <sup><sup>[top](#documentr_top)</sup></sup>



<a name="documentr_heading_18"></a>

## Required Information <sup><sup>[top](#documentr_top)</sup></sup>

 - plugin group - referred to as `«PLUGIN_GROUP»`
 - plugin name - referred to as `«PLUGIN_NAME»`
 - plugin package - referred to as `«PLUGIN.PACKAGE»`




<a name="documentr_heading_19"></a>

## Step 1 - create the `.properties` file <sup><sup>[top](#documentr_top)</sup></sup>

Create a file located at:



```
src/main/resources/META-INF/`«PLUGIN_GROUP»`.`«PLUGIN_NAME»`.properties
```



Enter the following details:



```
implementation-class=`«PLUGIN_GROUP»`.`«PLUGIN_NAME»`Plugin
```






<a name="documentr_heading_20"></a>

# Building the Package <sup><sup>[top](#documentr_top)</sup></sup>



<a name="documentr_heading_21"></a>

## *NIX/Mac OS X <sup><sup>[top](#documentr_top)</sup></sup>

From the root of the project, simply run

`./gradlew build`




<a name="documentr_heading_22"></a>

## Windows <sup><sup>[top](#documentr_top)</sup></sup>

`./gradlew.bat build`


This will compile and assemble the artefacts into the `build/libs/` directory.

Note that this may also run tests (if applicable see the Testing notes)



<a name="documentr_heading_23"></a>

# Running the Tests <sup><sup>[top](#documentr_top)</sup></sup>



<a name="documentr_heading_24"></a>

## *NIX/Mac OS X <sup><sup>[top](#documentr_top)</sup></sup>

From the root of the project, simply run

`gradle --info test`

if you do not have gradle installed, try:

`gradlew --info test`



<a name="documentr_heading_25"></a>

## Windows <sup><sup>[top](#documentr_top)</sup></sup>

From the root of the project, simply run

`gradle --info test`

if you do not have gradle installed, try:

`./gradlew.bat --info test`


The `--info` switch will also output logging for the tests



<a name="documentr_heading_26"></a>

## Dependencies - Gradle <sup><sup>[top](#documentr_top)</sup></sup>



```
dependencies {
	runtime(group: 'synapticloop', name: 'javaSample', version: '1.0.0', ext: 'jar')

	compile(group: 'synapticloop', name: 'javaSample', version: '1.0.0', ext: 'jar')
}
```



or, more simply for versions of gradle greater than 2.1



```
dependencies {
	runtime 'synapticloop:javaSample:1.0.0'

	compile 'synapticloop:javaSample:1.0.0'
}
```





<a name="documentr_heading_27"></a>

## Dependencies - Maven <sup><sup>[top](#documentr_top)</sup></sup>



```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>javaSample</artifactId>
	<version>1.0.0</version>
	<type>jar</type>
</dependency>
```





<a name="documentr_heading_28"></a>

## Dependencies - Downloads <sup><sup>[top](#documentr_top)</sup></sup>


You will also need to download the following dependencies:



### compile dependencies


**NOTE:** You may need to download any dependencies of the above dependencies in turn (i.e. the transitive dependencies)

--

> `This README.md file was hand-crafted with care utilising synapticloop`[`templar`](https://github.com/synapticloop/templar/)`->`[`documentr`](https://github.com/synapticloop/documentr/)

--
