package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO rewrite this thing
public class AddClassActivity extends Activity{

	ArrayList<String> term;
	ArrayList<String> major;
	ArrayList<String> course;
	ArrayList<String> classSections;
	ArrayAdapter<String> termAdapter;
	ArrayAdapter<String> majorAdapter;
	ArrayAdapter<String> courseAdapter;
	ArrayAdapter<String> sectionAdapter;
	
	AsyncHttpClient client;

	Spinner termSpinner;
	Spinner majorSpinner;
	Spinner courseSpinner;
	Spinner sectionSpinner;

	boolean	termSelected;
	boolean majorSelected;
	boolean courseSelected;
	boolean sectionSelected;
	
	String prevTerm;
	String prevMajor;
	String prevCourse;

	TextView time, timeResult;
	TextView professor, professorResult;
	TextView weekdays, weekdaysResult;

	Button add;

	HashMap<String, Section> classTable;
	Section selectedClass;

	Integer userID;
	
	String courseMajorShort;
	int	courseClassNum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addclass_activity);

		userID = getIntent().getIntExtra("userID", -1);
		if(userID == -1){
			Intent i = new Intent();
			setResult(RESULT_CANCELED, i);
			finish();
		}

		client = new AsyncHttpClient();
		term = new ArrayList<String>(1);
		major = new ArrayList<String>(1);
		course = new ArrayList<String>(1);
		classSections = new ArrayList<String>();

		time = (TextView) findViewById(R.id.times);
		timeResult = (TextView) findViewById(R.id.timeResult);

		weekdays = (TextView) findViewById(R.id.meeting);
		weekdaysResult = (TextView) findViewById(R.id.meetingResult);

		professor = (TextView) findViewById(R.id.professor);
		professorResult = (TextView) findViewById(R.id.professorResult);

		add = (Button) findViewById(R.id.addButton);
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				addClass();
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
			}


		});

		termSpinner = (Spinner) findViewById(R.id.termSpinner);
		getTerms();
		termSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String term = parent.getItemAtPosition(pos).toString();
				if(!term.equals("Select Term")){
					if(!majorSelected){
						getMajors(term);
					}
					majorSpinner.setEnabled(true);
				}else{
					majorSpinner.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		majorSpinner = (Spinner) findViewById(R.id.majorSpinner);
		majorSpinner.setEnabled(false);
		majorSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String major = parent.getItemAtPosition(pos).toString();
				if(!major.equals("Select A Major")){
					if(!courseSelected){
						getCourses(major);
						prevMajor = major;
						courseSelected = true;
					}else if(!major.equals(prevMajor)){
						getCourses(major);
						prevMajor = major;
					}
					courseSpinner.setEnabled(true);
				}else{
					courseSpinner.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
		courseSpinner.setEnabled(false);
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				String key = parent.getItemAtPosition(pos).toString();
				Scanner scanSelected = new Scanner(key);
				
				if(!key.equals("Select A Course")){
					if(!sectionSelected){
						String major_short = scanSelected.next();;
						Integer class_num = Integer.parseInt(scanSelected.next());
						
						getSections(major_short, class_num);
						prevCourse = key;
						sectionSelected = true;
					}else if(!key.equals(prevCourse)){
						String major_short = scanSelected.next();;
						Integer class_num = Integer.parseInt(scanSelected.next());
						
						getSections(major_short, class_num);
						prevCourse = key;
					}
					sectionSpinner.setEnabled(true);

				}else{
					sectionSpinner.setEnabled(false);
				}
				scanSelected.close();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);
		sectionSpinner.setEnabled(false);
		sectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				String section = parent.getItemAtPosition(pos).toString();
				if(!section.equals("Select A Section")){
					selectedClass = classTable.get(section);
					timeResult.setText(classTable.get(section).getFullTime());
					weekdaysResult.setText(classTable.get(section).getWeekdays());
					professorResult.setText(classTable.get(section).getInstructor());
					time.setVisibility(1);
					timeResult.setVisibility(1);
					weekdays.setVisibility(1);
					weekdaysResult.setVisibility(1);
					professor.setVisibility(1);
					professorResult.setVisibility(1);
					
					add.setEnabled(true);
				}else{
					time.setVisibility(4);
					timeResult.setVisibility(4);
					weekdays.setVisibility(4);
					weekdaysResult.setVisibility(4);
					professor.setVisibility(4);
					professorResult.setVisibility(4);
					add.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getTerms(){
		term = new ArrayList<String>();
		term.add("Select Term");

		client.get("http://www.lol-fc.com/classmate/getterms.php",
				new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){

				if(1 < response.length()){
					try{
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for(int i = 0; i < myjsonarray.length(); i++){
							jObj = myjsonarray.getJSONObject(i);
							term.add(jObj.getString("term"));
						}
					} catch (JSONException e){
						e.printStackTrace();
					}
				}
			}

		});
		termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, term);
		termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		termSpinner.setAdapter(termAdapter);

	}

	public void getMajors(String term){
		major = new ArrayList<String>();
		major.add("Select A Major");
		RequestParams params = new RequestParams();
		params.put("term", term);
		client.get("http://www.lol-fc.com/classmate/getmajors.php", params,
				new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){

				if(1 < response.length()){
					try{
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for(int i = 0; i < myjsonarray.length(); i++){
							jObj = myjsonarray.getJSONObject(i);
							major.add(jObj.getString("major_short"));
						}
					} catch (JSONException e){
						e.printStackTrace();
					}
				}
			}

		});
		majorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, major);
		majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		majorSpinner.setAdapter(majorAdapter);
	}

	public void getCourses(String majorShort){
		classTable = new HashMap<String, Section>(50);
		course = new ArrayList<String>();
		
		course.add("Select A Course");
		courseMajorShort = majorShort;
		
		RequestParams params = new RequestParams();
		params.put("major", majorShort);
		params.put("distinct", "yes");
		
		client.get("http://www.lol-fc.com/classmate/getclasses.php", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){

				if(1 < response.length()){
					try{
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for(int i = 0; i < myjsonarray.length(); i++){
							jObj = myjsonarray.getJSONObject(i);
							course.add(courseMajorShort + " " + jObj.getInt("class_num"));
						}
					} catch (JSONException e){
						e.printStackTrace();
					}
				}
			}
		});

		courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course);
		courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		courseSpinner.setAdapter(courseAdapter);
	}
	
	public void getSections(String majorShort, int classNum){
		classTable = new HashMap<String, Section>(50);
		classSections = new ArrayList<String>();
		classSections.add("Select A Section");
		Integer classNumber = classNum;
		
		RequestParams params = new RequestParams();
		params.put("major", majorShort);
		params.put("class_num", classNumber.toString());
		client.get("http://www.lol-fc.com/classmate/getclasses.php", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){
				if(1 < response.length()){
					try{
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for(int i = 0; i < myjsonarray.length(); i++){
							jObj = myjsonarray.getJSONObject(i);
							classSections.add(jObj.getString("title") + " Section " + jObj.getInt("section"));
							
							classTable.put(jObj.getString("title") + " Section " + jObj.getInt("section"), 
                                    new Section(jObj.getInt("class_id"),jObj.getString("title"), jObj.getString("time_start"), 
                                    		jObj.getString("time_end"),  jObj.getString("weekdays"),jObj.getString("date_start"), 
                                    		jObj.getString("date_end"), jObj.getString("instructor"),jObj.getInt("building"), 
                                    		jObj.getInt("room"), jObj.getInt("section"), jObj.getString("major_short"),
                                            jObj.getString("major_long"), jObj.getInt("class_num"), jObj.getString("term")));
						}
					}catch (JSONException e){
						e.printStackTrace();
					}
				}
			}
		});
		sectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classSections);
		sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sectionSpinner.setAdapter(sectionAdapter);
	}

	public void addClass(){
		Integer classID = selectedClass.getClassID();
		RequestParams params = new RequestParams();
		params.put("user_id", userID.toString());
		params.put("class_id", classID.toString());

		client.get("http://www.lol-fc.com/classmate/addclass.php", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){
				if( 1 < response.length()){
				}
			}
		});

	}

}