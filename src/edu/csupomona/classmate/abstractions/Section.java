package edu.csupomona.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;

public class Section implements Parcelable {
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
		if (major_long.equalsIgnoreCase("basket weaving")) {
			this.major_long = major_short;
		} else {
			this.major_long = major_long;
		}

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
		String startHour, endHour, ampmStart, ampmEnd;
		String startMinutes, endMinutes;
		startHour = time_start.substring(0, 2);
		endHour = time_end.substring(0, 2);
		startMinutes = time_start.substring(3,5);
		endMinutes = time_end.substring(3,5);
		Integer startHoursInt = Integer.parseInt(startHour);
		Integer endHoursInt = Integer.parseInt(endHour);
		Integer startMinutesInt = Integer.parseInt(startMinutes);
		Integer endMinutesInt = Integer.parseInt(endMinutes);


		/*
		 * 12-12 = 0
		 * 24-12 = 12
		 */

		ampmStart = " AM";
		ampmEnd = " AM";


		if(startHoursInt == 0){
			startHoursInt = 12;
		}else if (startHoursInt > 12) {
			startHoursInt = startHoursInt - 12;
			if(startHoursInt == 12){
				startHoursInt = 11;
				startMinutesInt = 59;
				startMinutes = startMinutesInt.toString();
			}
			ampmStart = " PM";
		}

		if(endHoursInt == 0){
			endHoursInt = 12;
		}else if (endHoursInt > 12) {
			endHoursInt = endHoursInt - 12;
			if(endHoursInt == 12){
				endHoursInt = 11;
				endMinutesInt = 59;
				endMinutes = endMinutesInt.toString();
			}
			ampmEnd = " PM";
		}

		startHour = startHoursInt.toString();
		endHour = endHoursInt.toString();

		return startHour + ":" +  startMinutes + ampmStart + " - " + endHour + ":" +  endMinutes + ampmEnd;
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

	public String getDateStart() {
		return date_start;
	}

	public String getDateEnd() {
		return date_end;
	}

	public String getBuilding() {
		return building;
	}

	public String getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return String.format("%s %s Section %s", major_short, class_num, section);
	}

	public String toString(int override) {
		return String.format("%s %s %s",getFullTime(), major_short, class_num);
	}

	private Section(Parcel in) {
		this.class_id = in.readInt();
		this.title = in.readString();
		this.time_start = in.readString();
		this.time_end = in.readString();
		this.weekdays = in.readString();
		this.date_start = in.readString();
		this.date_end = in.readString();
		this.instructor = in.readString();
		this.building = in.readString();
		this.room = in.readString();
		this.section = in.readString();
		this.major_short = in.readString();
		this.major_long = in.readString();
		this.class_num = in.readString();
		this.term = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(class_id);
		dest.writeString(title);
		dest.writeString(time_start);
		dest.writeString(time_end);
		dest.writeString(weekdays);
		dest.writeString(date_start);
		dest.writeString(date_end);
		dest.writeString(instructor);
		dest.writeString(building);
		dest.writeString(room);
		dest.writeString(section);
		dest.writeString(major_short);
		dest.writeString(major_long);
		dest.writeString(class_num);
		dest.writeString(term);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Section createFromParcel(Parcel in) {
			return new Section(in);
		}

		public Section[] newArray(int size) {
			return new Section[size];
		}
	};
}
