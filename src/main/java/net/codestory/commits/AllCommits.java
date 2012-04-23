package net.codestory.commits;

import com.google.common.base.*;
import net.codestory.cache.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.*;

import java.io.*;
import java.util.*;

public class AllCommits {
	private final String user;
	private final String project;

	public AllCommits(String user, String project) {
		this.user = user;
		this.project = project;
	}

	@Cached
	public List<RepositoryCommit> list() {
		try {
			return new CommitService().getCommits(new RepositoryService().getRepository(user, project));
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
