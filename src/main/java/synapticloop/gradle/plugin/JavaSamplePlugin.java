package synapticloop.gradle.plugin;

/*
 * Copyright (c) 2016 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * This is a sample plugin built in Java
 * 
 * @author synapticloop
 *
 */
public class JavaSamplePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		// this will register the extension point, such that in your gradle build 
		// file you can pass in configuration items into the task
		project.getExtensions().create("javaSample", JavaSamplePluginExtension.class);

		// Register the task named 'javaSampleHello', which can then be invoked 
		// with a simple gradle javaSampleHello
		project.getTasks().create("javaSampleHello", JavaSampleHelloTask.class);

		// Register the task named 'javaSampleGoodbye', which can then be invoked 
		// with a simple gradle javaSampleGoodbye
		project.getTasks().create("javaSampleGoodbye", JavaSampleGoodbyeTask.class);
	}
}
