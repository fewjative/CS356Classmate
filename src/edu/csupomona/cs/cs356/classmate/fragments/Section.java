package edu.csupomona.cs.cs356.classmate.fragments;

public class Section {

	private int class_id;
    private String title;
    private String time_start;
    private String time_end;
    private String weekdays;
    private String date_start;
    private String date_end;
    private String instructor;
    private int building;
    private int room;
    private int section;
    private String major_short;
    private String major_long;
    private int class_num;
    private String term;

    public Section() {
            this.class_id = 0;
            this.title = null;
            this.time_start = null;
            this.time_end = null;
            this.weekdays = null;
            this.date_start = null;
            this.date_end = null;
            this.instructor = null;
            this.building = 0;
            this.room = 0;
            this.section = 0;
            this.major_short = null;
            this.major_long = null;
            this.class_num = 0;
            this.term = null;
    }

    public Section(int class_id, String title, String time_start,
                    String time_end, String weekdays, String date_start, String date_end,
                    String instructor, int building, int room, int section,
                    String major_short, String major_long, int class_num, String term) {

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
    
    public String getTitle(){
            return title;
    }
    
    public String getStartTime(){
            return time_start;
    }
    
    public String getEndTime(){
            return time_end;
    }
    
    public String getWeekdays(){
            return weekdays;
    }
    
    public String getInstructor(){
            return instructor;
    }
    
    public String getFullTime(){
            String start, end, amPmStart, amPmEnd;
            String startEnd, endEnd;
            start = time_start.substring(0,2);
            end = time_end.substring(0,2);
            Integer startInt = Integer.parseInt(start);
            Integer endInt = Integer.parseInt(end);
            
            amPmStart = " AM";
            amPmEnd = " AM";
            if(startInt > 12){
                    startInt = startInt-12;
                    amPmStart = " PM";
            }
            
            if(endInt > 12){
                    endInt = startInt-12;
                    amPmEnd = " PM";
            }
            start = startInt.toString();
            end = endInt.toString();
            startEnd = time_start.substring(2, 5);
            endEnd = time_end.substring(2,5);
            
            return start + startEnd + amPmStart + " - " + end + endEnd + amPmEnd;
    }
    
    public int getClassID(){
            return class_id;
    }
    
    public String getMajorShort(){
    	return major_short;
    }
    
    public int getClassNum(){
    	return class_num;
    }
    
    public int getSection(){
    	return section;
    }
}
