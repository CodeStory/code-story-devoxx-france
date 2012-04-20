package net.codestory;

import lombok.*;

@Data
public class Commit {
	private final String sha1;
	private final String author;
	private final String gravatarUrl;
	private final String message;
	private final String date;
	private final String status;
}
