package net.codestory;

import org.eclipse.egit.github.core.*;

import java.util.*;

public class AllCommits {
	private final AllGitHubCommits allGitHubCommits;

	public AllCommits(String user, String project) {
		allGitHubCommits = new AllGitHubCommits(user, project);
	}

	public List<RepositoryCommit> list() {
		return allGitHubCommits.fetchCommitFromGitHub();
	}
}
