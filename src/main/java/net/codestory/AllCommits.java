package net.codestory;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import java.io.*;
import java.util.*;

import static com.google.common.collect.Lists.*;

public class AllCommits {

	public List<Commit> list() {
		GitHubClient githubClient = new GitHubClient("github", -1, "http");
		RepositoryService repositoryService = new RepositoryService(githubClient);
		CommitService commitService = new CommitService(githubClient);
		Repository repository = null;
		try {
			repository = repositoryService.getRepository("dgageot", "CodeStory");
			commitService.getCommits(repository);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newArrayList(new Commit("dgageot", "first commit"), new Commit("jlmorlhon", "second commit"), new Commit("seblm", "third commit"));
	}
}
