package net.codestory;

import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class AllCommits {
	private String user;
	private String project;

	LoadingCache<String, List<RepositoryCommit>> cache = CacheBuilder.newBuilder() //
			.refreshAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, List<RepositoryCommit>>() {
				@Override
				public List<RepositoryCommit> load(String key) throws Exception {
					try {
						System.out.println("GITHUB");
						GitHubClient githubClient = new GitHubClient("github", -1, "http");
						CommitService commits = new CommitService(githubClient);
						RepositoryService repository = new RepositoryService(githubClient);
						return commits.getCommits(repository.getRepository(user, project));
					} catch (IOException e) {
						throw Throwables.propagate(e);
					}
				}

				@Override
				public ListenableFuture<List<RepositoryCommit>> reload(final String key, List<RepositoryCommit> oldValue) throws Exception {
					return ListenableFutureTask.create(new Callable<List<RepositoryCommit>>() {
						@Override
						public List<RepositoryCommit> call() throws Exception {
							return load(key);
						}
					});
				}
			});

	public AllCommits(String user, String project) {
		this.user = user;
		this.project = project;
	}

	public List<RepositoryCommit> list() {
		return cache.getUnchecked("KEY");
	}
}
