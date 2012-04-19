package net.gageot;

import com.google.common.collect.*;

import java.util.*;

public class AllCommits {

	public List<Commit> list() {
		return Lists.newArrayList(new Commit("dgageot"), new Commit("jlmorlhon"), new Commit("seblm"));
	}
}
