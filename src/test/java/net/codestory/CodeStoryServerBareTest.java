package net.codestory;

import org.junit.*;

public class CodeStoryServerBareTest {
	@Test
	public void should_startup_with_no_arguments_without_failing() {
		CodeStoryServer.main(null);
	}
}
