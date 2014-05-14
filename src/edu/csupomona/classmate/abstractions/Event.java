package edu.csupomona.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
	private final int event_id;
	private final String title;
	private final String description;
	private final String time_start;
	private final String time_end;
	private final String date_start;
	private final String date_end;
	private final String weekdays;
	private final String fpublic;//if this is 1, the event can only be seen by friends
	private final String opublic;//if this is 1, the event can be seen by everyone(o=open)
	private final String isprivate;//if this is 1, it cannot be seen by anyone
	
	public Event(
			int event_id,
			String title,
			String description,
			String time_start,
			String time_end,
			String date_start,
			String date_end,
			String weekdays,
			String fpublic,
			String opublic,
			String isprivate
	) {
		this.event_id = event_id;
		this.title = title;
		this.description = description;
		this.time_start = time_start;
		this.time_end = time_end;
		this.date_start = date_start;
		this.date_end = date_end;
		this.weekdays = weekdays;
		this.fpublic = fpublic;
		this.opublic = opublic;
		this.isprivate = isprivate;
	}

	public int getID()
	{
		return event_id;
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

	public String getDescription() {
		return description;
	}
	
	public String getDateStart() {
		return date_start;
	}

	public String getDateEnd() {
		return date_end;
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

		return date_start + " " + startHour + ":" +  startMinutes + ampmStart + " - " + date_end +" " + endHour + ":" +  endMinutes + ampmEnd;
	}


	public String getOpublic() {
		return opublic;
	}

	public String getFpublic() {
		return fpublic;
	}
	
	public String getIsprivate() {
		return isprivate;
	}
	

	public String toString() {
		return String.format("%s %s",title, getFullTime());
	}

	private Event(Parcel in) {
		this.event_id = in.readInt();
		this.title = in.readString();
		this.description = in.readString();
		this.time_start = in.readString();
		this.time_end = in.readString();
		this.date_start = in.readString();
		this.date_end = in.readString();
		this.weekdays = in.readString();
		this.fpublic = in.readString();
		this.opublic = in.readString();
		this.isprivate = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(event_id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(time_start);
		dest.writeString(time_end);
		dest.writeString(date_start);
		dest.writeString(date_end);
		dest.writeString(weekdays);
		dest.writeString(fpublic);
		dest.writeString(opublic);
		dest.writeString(isprivate);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
}
