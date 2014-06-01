package edu.csupomona.classmate.abstractions;

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
String startHour, endHour, ampmStart, ampmEnd;
String startMinutes, endMinutes;
startHour = time_start.substring(0, 2);
startMinutes = time_start.substring(3, 5);
endHour = time_end.substring(0, 2);
endMinutes = time_end.substring(3, 5);
Integer startHourInt = Integer.parseInt(startHour);
Integer startMinutesInt = Integer.parseInt(startMinutes);
Integer endHourInt = Integer.parseInt(endHour);
Integer endMinutesInt = Integer.parseInt(endMinutes);

ampmStart = " AM";
ampmEnd = " AM";

if(startHourInt == 0){
startHourInt = 12;
}else if (startHourInt > 12) {
startHourInt = startHourInt - 12;
if(startHourInt == 12){
startHourInt = 11;
startMinutesInt = 59;
startMinutes = startMinutesInt.toString();
}
ampmStart = " PM";
}

if(endHourInt == 0){
endHourInt = 12;
}else if (endHourInt > 12) {
endHourInt = endHourInt - 12;
if(endHourInt == 12){
endHourInt = 11;
endMinutesInt = 59;
endMinutes = endMinutesInt.toString();
}
ampmEnd = " PM";
}

startHour = startHourInt.toString();
endHour = endHourInt.toString();



return startHour + ":" + startMinutes + ampmStart + " - " + endHour + ":" + endMinutes + ampmEnd;
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