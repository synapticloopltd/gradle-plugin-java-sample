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

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

public class JavaSampleGoodbyeTask extends DefaultTask {
	private Logger logger;
	private JavaSamplePluginExtension extension;
	/**
	 * Instantiate a new Java Sample Goodbye Task and set the group and description
	 */
	public JavaSampleGoodbyeTask() {
		setGroup("Sample");
		setDescription("This is a sample plugin that says goodbye");

		this.logger = getProject().getLogger();
	}

	@TaskAction
	public void generate() {
		extension = getProject().getExtensions().findByType(JavaSamplePluginExtension.class);

		if (extension == null) {
			extension = new JavaSamplePluginExtension();
		}

		logger.info("Invoking the goodbye task");

		output();

		if(extension.getRepeat()) {
			output();
		}
	}

	private void output() {
		if(extension.getNames().size() != 0) {
			for(String name: extension.getNames()) {
				logger.lifecycle(String.format("%s says: '%s'", name, extension.getGoodbyeText()));
			}
		} else {
			logger.lifecycle(extension.getGoodbyeText());
		}
	}
}
