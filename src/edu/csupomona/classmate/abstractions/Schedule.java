package edu.csupomona.classmate.abstractions;

public class Schedule {
	private final String schedule;
	private final int schedule_id;

	public Schedule(int schedule_id,String schedule) {
		this.schedule = schedule;
		this.schedule_id = schedule_id;
	}

	public String getSchedule() {
		return schedule;
	}
	
	public int getScheduleID(){
		return schedule_id;
	}

	@Override
	public String toString() {
		return schedule;
	}
}
