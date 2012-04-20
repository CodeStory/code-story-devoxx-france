package net.codestory;

import com.google.common.base.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;
import org.joda.time.format.*;

import java.io.*;
import java.util.*;

import static com.google.common.base.Objects.*;
import static com.google.common.collect.Lists.*;

public class AllCommits {
	private static final String USER = "jlm";
	private static final String PROJECT = "NodeGravatar";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");

	public List<Commit> list() {
		GitHubClient githubClient = new GitHubClient("github", -1, "http");
		CommitService commits = new CommitService(githubClient);
		RepositoryService repository = new RepositoryService(githubClient);

		try {
			List<RepositoryCommit> githubCommits = commits.getCommits(repository.getRepository(USER, PROJECT));
			return transform(githubCommits, TO_COMMIT);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private static final User NULL_USER = new User().setLogin("").setAvatarUrl("");
	private static final org.eclipse.egit.github.core.Commit NULL_COMMIT_USER = new org.eclipse.egit.github.core.Commit().setAuthor(new CommitUser().setDate(new Date()));

	static Function<RepositoryCommit, Commit> TO_COMMIT = new Function<RepositoryCommit, Commit>() {

		@Override
		public Commit apply(RepositoryCommit repositoryCommit) {
			User committer = firstNonNull(repositoryCommit.getCommitter(), NULL_USER);
			org.eclipse.egit.github.core.Commit commit = firstNonNull(repositoryCommit.getCommit(), NULL_COMMIT_USER);

			return new Commit( //
					committer.getLogin(), //
					committer.getAvatarUrl().split("\\?")[0], //
					commit.getMessage(), //
					format(commit.getAuthor().getDate()) //
			);
		}

		private String format(Date date) {
			return DATE_FORMATTER.print(date.getTime());
		}
	};
}
