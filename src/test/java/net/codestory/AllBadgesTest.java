package net.codestory;

import org.eclipse.egit.github.core.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AllBadgesTest {

	@Mock AllCommits allCommits;
	@InjectMocks AllBadges allBadges;

	@Test
	public void should_show_top_committer() throws Exception {
		when(allCommits.list()).thenReturn(Arrays.asList( //
				new RepositoryCommit().setAuthor(new User().setLogin("dgageot").setAvatarUrl("https://secure.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cf")), //
				new RepositoryCommit().setAuthor(new User().setLogin("dgageot").setAvatarUrl("https://secure.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cf")), //
				new RepositoryCommit().setAuthor(new User().setLogin("jlm").setAvatarUrl("https://secure.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cfkjsdkfj"))));

		Badge topCommitterBadge = allBadges.list().get(0);

		assertThat(topCommitterBadge.getGravatarUrl()).isEqualTo("https://secure.gravatar.com/avatar/f0887bf6175ba40dca795eb37883a8cf");
	}
}
