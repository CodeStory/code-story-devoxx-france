package net.codestory;

import com.google.common.base.*;
import com.google.inject.*;
import com.sun.jersey.api.*;
import net.codestory.badges.*;
import net.codestory.github.*;
import org.eclipse.egit.github.core.*;
import org.lesscss.*;

import javax.activation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.base.Objects.*;
import static net.codestory.misc.DateFormat.*;
import static net.gageot.listmaker.ListMaker.*;

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
	public Iterable<net.codestory.github.Commit> commits() {
		return with(allCommits.list()).to(TO_COMMIT);
	}

	@GET
	@Path("badges")
	@Produces("application/json;charset=UTF-8")
	public Iterable<Badge> badges() {
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

	static Function<RepositoryCommit, net.codestory.github.Commit> TO_COMMIT = new Function<RepositoryCommit, net.codestory.github.Commit>() {
		@Override
		public net.codestory.github.Commit apply(RepositoryCommit repositoryCommit) {
			User committer = firstNonNull(repositoryCommit.getCommitter(), new User().setLogin(""));
			org.eclipse.egit.github.core.Commit commit = firstNonNull(repositoryCommit.getCommit(), new org.eclipse.egit.github.core.Commit());
			commit.setAuthor(firstNonNull(commit.getAuthor(), new CommitUser().setDate(new Date())));
			String avatarUrl = firstNonNull(committer.getAvatarUrl(), "");

			return new net.codestory.github.Commit( //
					repositoryCommit.getSha(), //
					committer.getLogin(), //
					avatarUrl.split("\\?")[0], //
					commit.getMessage(), //
					format(commit.getAuthor().getDate()), //
					firstNonNull(commit.getSha(), "UNKNOWN").isEmpty() ? "FAILURE" : "SUCCESS");
		}
	};
}
