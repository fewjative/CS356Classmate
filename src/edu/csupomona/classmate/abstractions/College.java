package edu.csupomona.classmate.abstractions;

public class College {
	private final String major_short;
	private final String major_long;

	public College(String major_short, String major_long) {
		this.major_short = major_short;
		if (major_long != null && major_long.equalsIgnoreCase("basket weaving")) {
			this.major_long = major_short;
		} else {
			this.major_long = major_long;
		}
	}

	public String getMajorShort() {
		return major_short;
	}

	public String getMajorLong() {
		return major_long;
	}

	@Override
	public String toString() {
		return major_long;
	}
}
