import net.gageot.test.utils.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

public class CodeStoryServerTest {
	@Test
	public void can() {
		int returnCode = new Shell().execute("./mocha.sh testListeCommits.js");

		assertThat(returnCode).isZero();
	}
}
