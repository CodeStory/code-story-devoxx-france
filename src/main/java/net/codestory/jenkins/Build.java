package net.codestory.jenkins;

import lombok.*;

@Data
public class Build {
	public final String result;
	public final ChangeSet changeSet;
}
