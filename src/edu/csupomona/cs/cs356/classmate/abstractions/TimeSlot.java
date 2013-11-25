package edu.csupomona.cs.cs356.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeSlot implements Parcelable {
	
	private final String time_start;
	private final String time_end;
	

	public TimeSlot(
			
			String time_start,
			String time_end
	) {
		
		this.time_start = time_start;
		this.time_end = time_end;
		
	}


	public String getStartTime() {
		return time_start;
	}

	public String getEndTime() {
		return time_end;
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

	
	@Override
	public String toString() {
		//return String.format("%s %s Section %s", major_short, class_num, section);
		return String.format("%s", getFullTime());
	}

	private TimeSlot(Parcel in) {
		
		this.time_start = in.readString();
		this.time_end = in.readString();
		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	
		dest.writeString(time_start);
		dest.writeString(time_end);
		
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public TimeSlot createFromParcel(Parcel in) {
			return new TimeSlot(in);
		}

		public TimeSlot[] newArray(int size) {
			return new TimeSlot[size];
		}
	};
}
