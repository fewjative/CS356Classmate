package edu.csupomona.cs.cs356.classmate.abstractions;

public class Course {
	public static final Course NULL_COURSE;
	static {
		NULL_COURSE = new Course(
			  new College("<all>", null),
			  ""
		);
	}

	public final College college;
	public final String class_num;

	public Course(
			College college,
			String class_num
	) {
		this.college = college;
		this.class_num = class_num;
	}

	public College getCollege() {
		return college;
	}

	public String getClassNum() {
		return class_num;
	}

	@Override
	public String toString() {
		return college.getMajorShort() + " " + class_num;
	}
}
