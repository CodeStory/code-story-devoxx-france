package net.codestory;

import com.google.common.collect.*;
import org.eclipse.egit.github.core.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AllBadgesTest {
	@Mock AllCommits allCommits;
	@InjectMocks AllBadges allBadges;

	@Test
	public void should_get_all_badges() throws Exception {
		given(allCommits.list()).willReturn(Lists.newArrayList(new RepositoryCommit()));

		List<Badge> badges = allBadges.list();

		assertThat(badges).onProperty("label").containsSequence("Top Committer", "Fatty Committer");
		assertThat(badges).onProperty("image").containsSequence("TopCommitter.png", "FattyCommitter.png");
	}
}
