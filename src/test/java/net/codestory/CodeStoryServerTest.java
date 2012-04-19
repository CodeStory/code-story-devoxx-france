package net.codestory;

import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.junit.*;

import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;

public class CodeStoryServerTest {
	@ClassRule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class);

	private static int port() {
		return codeStoryServer.service().getPort();
	}

	@Test
	public void can_show_commit_list() {
		int exitCode = new Shell().execute("./mocha.sh testListeCommits.js " + port());

		assertThat(exitCode).isZero();
	}
}
