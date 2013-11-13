package edu.csupomona.cs.cs356.classmate.fragments;

public class Course {
	static final Course NULL_COURSE;
	static {
		NULL_COURSE = new Course();
		NULL_COURSE.college = new College();
		NULL_COURSE.college.major_short = "<all>";

		NULL_COURSE.class_num = "";
	}

	College college;
	String class_num;

	Course() {
		//...
	}

	@Override
	public String toString() {
		return college.major_short + " " + class_num;
	}
}
