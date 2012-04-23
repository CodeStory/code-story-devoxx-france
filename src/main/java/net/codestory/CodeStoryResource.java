package net.codestory;

import com.google.common.base.*;
import com.google.inject.*;
import com.sun.jersey.api.*;
import net.codestory.badges.*;
import net.codestory.github.*;
import net.codestory.github.Commit;
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
	public List<Commit> commits() {
		return with(allCommits.list()).to(TO_COMMIT).toList();
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
		buildCacheControl();
		return Response.ok(file, mimeType).cacheControl(buildCacheControl()).lastModified(new Date()).build();
	}

	private CacheControl buildCacheControl() {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(3600 * 24 * 30); // 1 month
		cacheControl.setNoTransform(false); // bug is jax-rs
		return cacheControl;
	}

	static Function<RepositoryCommit, Commit> TO_COMMIT = new Function<RepositoryCommit, Commit>() {
		@Override
		public Commit apply(RepositoryCommit githubCommit) {
			User author = firstNonNull(githubCommit.getAuthor(), new User().setLogin(""));
			String avatarUrl = firstNonNull(author.getAvatarUrl(), "");
			String sha1 = firstNonNull(githubCommit.getSha(), "UNKNOWN");
			org.eclipse.egit.github.core.Commit commit = firstNonNull(githubCommit.getCommit(), new org.eclipse.egit.github.core.Commit().setCommitter(new CommitUser().setDate(new Date())));

			return new Commit( //
					githubCommit.getSha(), //
					author.getLogin(), //
					avatarUrl.split("\\?")[0], //
					commit.getMessage(), //
					format(commit.getAuthor().getDate()), //
					sha1.isEmpty() ? "FAILURE" : "SUCCESS");
		}
	};
}
