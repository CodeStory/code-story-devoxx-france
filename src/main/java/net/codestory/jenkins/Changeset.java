package net.codestory.jenkins;

import lombok.*;

import java.util.*;

@Data
public class ChangeSet {
	private final List<Item> items;
}
