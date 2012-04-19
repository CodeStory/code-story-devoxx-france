import net.gageot.test.rules.*;
import net.gageot.test.utils.*;
import org.junit.*;

import static net.gageot.test.rules.ServiceRule.startWithRandomPort;
import static org.fest.assertions.Assertions.*;

public class CodeStoryServerTest {
	@Rule
	public static ServiceRule<CodeStoryServer> codeStoryServer = startWithRandomPort(CodeStoryServer.class);

	@Test
	public void can() {
		int returnCode = new Shell().execute("./mocha.sh testListeCommits.js " + codeStoryServer.service().getPort());

		assertThat(returnCode).isZero();
	}
}
