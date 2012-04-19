package net.codestory;

import java.util.*;

import static com.google.common.collect.Lists.*;

public class AllCommits {

	public List<Commit> list() {
		return newArrayList(new Commit("dgageot", "first commit"), new Commit("jlmorlhon", "second commit"), new Commit("seblm", "third commit"));
	}
}
