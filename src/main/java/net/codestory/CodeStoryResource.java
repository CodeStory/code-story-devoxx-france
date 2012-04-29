package net.codestory;

import com.google.common.base.*;
import com.google.common.io.*;
import com.google.inject.*;
import com.sun.jersey.api.*;
import net.codestory.badges.*;
import net.codestory.commits.*;
import net.codestory.commits.Commit;
import org.eclipse.egit.github.core.*;
import org.lesscss.*;

import javax.activation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.base.Charsets.*;
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
	public Iterable<Commit> commits() {
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
		return new LessCompiler().compile(file(path));
	}

	@GET
	@Path("codestory.js")
	@Produces("application/javascript;charset=UTF-8")
	public String javascript() throws IOException {
		return Files.toString(file("mustache.js"), UTF_8) + //
				Files.toString(file("jquery.js"), UTF_8) + //
				Files.toString(file("codestory.js"), UTF_8);
	}

	@GET
	@Path("{path : .*}")
	public Response staticResource(@PathParam("path") String path) throws IOException {
		File file = file(path);
		String mimeType = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mimeType).cacheControl(buildCacheControl()).lastModified(new Date()).build();
	}

	static File file(String path) throws IOException {
		File root = new File("web");
		File file = new File(root, path);
		if (!file.exists() || !file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
			throw new NotFoundException();
		}
		return file;
	}

	static CacheControl buildCacheControl() {
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
					sha1.isEmpty() ? "FAILURE" : "SUCCESS", //
					githubCommit.getUrl().replaceFirst("https://api.github.com/repos/", "https://github.com/"));
		}
	};
}
