package net.codestory.jenkins;

import com.google.common.base.*;
import com.google.common.io.*;
import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AllBuilds {
	public List<Build> list() throws IOException {
		URL url = new URL("http://jenkins.code-story.net:8888/job/CodeStory/api/json?depth=1");
		String json = Resources.toString(url, Charsets.UTF_8);

		Builds builds = new Gson().fromJson(json, Builds.class);

		return builds.getBuild();
	}
}
