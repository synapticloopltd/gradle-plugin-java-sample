package synapticloop.gradle.plugin;

import java.util.ArrayList;
import java.util.List;

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

public class JavaSamplePluginExtension {
	private String helloText = "Hello World!";
	private String goodbyeText = "Good-bye Cruel World!";
	private boolean repeat = false;
	private List<String> names = new ArrayList<String>();

	public String getHelloText() { return this.helloText; }

	public void setHelloText(String helloText) { this.helloText = helloText; }

	public String getGoodbyeText() { return this.goodbyeText; }

	public void setGoodbyeText(String goodbyeText) { this.goodbyeText = goodbyeText; }

	public boolean getRepeat() { return this.repeat; }

	public void setRepeat(boolean repeat) { this.repeat = repeat; }

	public List<String> getNames() { return this.names; }

	public void setNames(List<String> names) { this.names = names; }
}
