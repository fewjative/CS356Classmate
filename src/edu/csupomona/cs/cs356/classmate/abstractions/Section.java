package edu.csupomona.cs.cs356.classmate.abstractions;

public class Section {
	private final int class_id;
	private final String title;
	private final String time_start;
	private final String time_end;
	private final String weekdays;
	private final String date_start;
	private final String date_end;
	private final String instructor;
	private final String building;
	private final String room;
	private final String section;
	private final String major_short;
	private final String major_long;
	private final String class_num;
	private final String term;

	public Section(
			int class_id,
			String title,
			String time_start,
			String time_end,
			String weekdays,
			String date_start,
			String date_end,
			String instructor,
			String building,
			String room,
			String section,
			String major_short,
			String major_long,
			String class_num,
			String term
	) {
		this.class_id = class_id;
		this.title = title;
		this.time_start = time_start;
		this.time_end = time_end;
		this.weekdays = weekdays;
		this.date_start = date_start;
		this.date_end = date_end;
		this.instructor = instructor;
		this.building = building;
		this.room = room;
		this.section = section;
		this.major_short = major_short;
		this.major_long = major_long;
		this.class_num = class_num;
		this.term = term;
	}

	public String getTitle() {
		return title;
	}

	public String getStartTime() {
		return time_start;
	}

	public String getEndTime() {
		return time_end;
	}

	public String getWeekdays() {
		return weekdays;
	}

	public String getInstructor() {
		return instructor;
	}

	public String getFullTime() {
		String start, end, amPmStart, amPmEnd;
		String startEnd, endEnd;
		start = time_start.substring(0, 2);
		end = time_end.substring(0, 2);
		Integer startInt = Integer.parseInt(start);
		Integer endInt = Integer.parseInt(end);

		amPmStart = " AM";
		amPmEnd = " AM";
		if (startInt > 12) {
			startInt = startInt - 12;
			amPmStart = " PM";
		}

		if (endInt > 12) {
			endInt = endInt - 12;
			amPmEnd = " PM";
		}
		start = startInt.toString();
		end = endInt.toString();
		startEnd = time_start.substring(2, 5);
		endEnd = time_end.substring(2, 5);

		return start + startEnd + amPmStart + " - " + end + endEnd + amPmEnd;
	}

	public int getClassID() {
		return class_id;
	}

	public String getMajorShort() {
		return major_short;
	}

	public String getClassNum() {
		return class_num;
	}

	public String getSection() {
		return section;
	}
}