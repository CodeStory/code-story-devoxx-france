package net.codestory;

import com.google.common.base.*;
import com.google.inject.*;
import com.sun.jersey.api.*;
import org.eclipse.egit.github.core.*;
import org.lesscss.*;

import javax.activation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.base.Objects.*;
import static com.google.common.collect.Lists.*;
import static net.codestory.DateFormat.*;

@Path("/")
public class CodeStoryResource {
	@Inject AllCommits allCommits;
	@Inject AllBadges allBadges;

	@GET
	public Response index() {
		return Response.temporaryRedirect(URI.create("index.html")).build();
	}

	@GET
	@Path("commits")
	@Produces("application/json;charset=UTF-8")
	public List<Commit> commits() {
		return transform(allCommits.list(), TO_COMMIT);
	}

	@GET
	@Path("badges")
	@Produces("application/json;charset=UTF-8")
	public List<Badge> badges() {
		return allBadges.list();
	}

	@GET
	@Path("{path : .*\\.less}")
	public String style(@PathParam("path") String path) throws IOException, LessException {
		return new LessCompiler().compile(new File("web", path));
	}

	@GET
	@Path("{path : .*}")
	public Response staticResource(@PathParam("path") String path) {
		File file = new File("web", path);
		if (!file.exists()) {
			throw new NotFoundException();
		}
		String mimeType = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mimeType).build();
	}

	static Function<RepositoryCommit, Commit> TO_COMMIT = new Function<RepositoryCommit, Commit>() {
		@Override
		public Commit apply(RepositoryCommit repositoryCommit) {
			User committer = firstNonNull(repositoryCommit.getCommitter(), new User().setLogin(""));
			org.eclipse.egit.github.core.Commit commit = firstNonNull(repositoryCommit.getCommit(), new org.eclipse.egit.github.core.Commit());
			commit.setAuthor(firstNonNull(commit.getAuthor(), new CommitUser().setDate(new Date())));
			String avatarUrl = firstNonNull(committer.getAvatarUrl(), "");

			return new Commit( //
					repositoryCommit.getSha(), //
					committer.getLogin(), //
					avatarUrl.split("\\?")[0], //
					commit.getMessage(), //
					format(commit.getAuthor().getDate()), //
					firstNonNull(commit.getSha(), "UNKNOWN").isEmpty() ? "FAILURE" : "SUCCESS");
		}
	};
}
