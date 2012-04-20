package net.codestory;

import com.google.common.base.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import java.io.*;
import java.util.*;

public class AllCommits {
	private String user;
	private String project;

	public AllCommits(String user, String project) {
		this.user = user;
		this.project = project;
	}

	public List<RepositoryCommit> list() {
		GitHubClient githubClient = new GitHubClient("github", -1, "http");
		CommitService commits = new CommitService(githubClient);
		RepositoryService repository = new RepositoryService(githubClient);

		try {
			return commits.getCommits(repository.getRepository(user, project));
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
