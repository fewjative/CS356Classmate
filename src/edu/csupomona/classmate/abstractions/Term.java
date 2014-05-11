package edu.csupomona.classmate.abstractions;

public class Term {
	private final String term;

	public Term(String term) {
		this.term = term;
	}

	public String getTerm() {
		return term;
	}

	@Override
	public String toString() {
		return term;
	}
}
