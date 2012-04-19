package net.codestory;

import com.google.common.base.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;
import org.joda.time.format.*;

import java.io.*;
import java.util.*;

import static com.google.common.collect.Lists.*;

public class AllCommits {
	private static final String USER = "jlm";
	private static final String PROJECT = "NodeGravatar";
	private static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");

	public List<Commit> list() {
		GitHubClient githubClient = new GitHubClient("github", -1, "http");
		CommitService commits = new CommitService(githubClient);
		RepositoryService repository = new RepositoryService(githubClient);

		try {
			return transform(commits.getCommits(repository.getRepository(USER, PROJECT)), TO_COMMIT);

		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private static Function<RepositoryCommit, Commit> TO_COMMIT = new Function<RepositoryCommit, Commit>() {
		@Override
		public Commit apply(RepositoryCommit repositoryCommit) {
			return new Commit(//
					repositoryCommit.getCommitter().getLogin(), //
					repositoryCommit.getCommitter().getAvatarUrl().split("\\?")[0], //
					repositoryCommit.getCommit().getMessage(), //
					format(repositoryCommit.getCommit().getAuthor().getDate()) //
			);
		}

		private String format(Date date) {
			return DATE_FORMATTER.print(date.getTime());
		}
	};
}
