package net.codestory.jenkins;

import com.google.gson.*;

import java.io.*;
import java.util.*;

import static com.google.common.io.Closeables.*;

public class AllBuilds {
	// http://jenkins.code-story.net:8888/job/CodeStory/api/json?depth=1
	private static final String API_URL = "src/test/resources/builds.json";

	public List<Build> list() throws IOException {
		FileReader reader = null;
		try {
			reader = new FileReader(API_URL);

			return parseJson(reader);
		} finally {
			closeQuietly(reader);
		}
	}

	private static List<Build> parseJson(FileReader reader) {
		Builds builds = new Gson().fromJson(reader, Builds.class);

		return builds.getBuilds();
	}
}
