package net.codestory.github;

import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class AllCommits {
	private final String user;
	private final String project;
	private LoadingCache<String, List<RepositoryCommit>> cache;

	public AllCommits(String user, String project) {
		this.user = user;
		this.project = project;
		cache = CacheBuilder.newBuilder().refreshAfterWrite(1, MINUTES) //
				.build(new CacheLoader<String, List<RepositoryCommit>>() {
					@Override
					public List<RepositoryCommit> load(String key) {
						return fetchCommits();
					}

					@Override
					public ListenableFuture<List<RepositoryCommit>> reload(final String key, List<RepositoryCommit> oldValue) {
						return ListenableFutureTask.create(new Callable<List<RepositoryCommit>>() {
							@Override
							public List<RepositoryCommit> call() {
								return fetchCommits();
							}
						});
					}
				});
	}

	public List<RepositoryCommit> list() {
		return cache.getUnchecked("COMMITS");
	}

	private List<RepositoryCommit> fetchCommits() {
		try {
			return new CommitService().getCommits(new RepositoryService().getRepository(user, project));
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
