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

	static Function<RepositoryCommit, Commit> TO_COMMIT = new Function<RepositoryCommit, Commit>() {
		@Override
		public Commit apply(RepositoryCommit repositoryCommit) {
			User committer = repositoryCommit.getCommitter();
			if (committer == null) {
				committer = new User().setLogin("");
			}
			org.eclipse.egit.github.core.Commit commit = repositoryCommit.getCommit();
			if (commit == null) {
				commit = new org.eclipse.egit.github.core.Commit().setAuthor(new CommitUser().setDate(new Date()));
			}
			String avatarUrl = committer.getAvatarUrl();
			if (avatarUrl == null) {
				avatarUrl = "";
			}
			return new Commit(//
					committer.getLogin(), //
					avatarUrl.split("\\?")[0], //
					commit.getMessage(), //
					format(commit.getAuthor().getDate()) //
			);
		}

		private String format(Date date) {
			return DATE_FORMATTER.print(date.getTime());
		}
	};
}
