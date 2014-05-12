package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.College;
import edu.csupomona.cs.cs356.classmate.abstractions.Course;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import edu.csupomona.cs.cs356.classmate.abstractions.Term;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEventActivity extends Activity {
	private EditText textFieldDescription;
	private EditText textFieldTitle;
	private RadioButton radioButtonOPublic;
	private RadioButton radioButtonFPublic;
	private RadioButton radioButtonPrivate;
	private TimePicker timePickerStartTime, timePickerEndTime;
	private DatePicker datePickerStartDate, datePickerEndDate;
	private Button btnCreateEvent;
	final Context context = this;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent_activity);
		setResult(RESULT_CANCELED);

		final int id = getIntent().getIntExtra("userID", NULL_USER);

		textFieldDescription = (EditText)findViewById(R.id.textFieldDescription);
		textFieldTitle = (EditText)findViewById(R.id.textFieldTitle);
		radioButtonOPublic = (RadioButton)findViewById(R.id.radioButtonOPublic);
		radioButtonFPublic = (RadioButton)findViewById(R.id.radioButtonFPublic);
		radioButtonPrivate = (RadioButton)findViewById(R.id.radioButtonPrivate);
		timePickerStartTime = (TimePicker)findViewById(R.id.timePickerStartTime);
		timePickerEndTime = (TimePicker)findViewById(R.id.timePickerEndTime);
		datePickerStartDate = (DatePicker)findViewById(R.id.datePickerStartDate);
		datePickerEndDate = (DatePicker)findViewById(R.id.datePickerEndDate);
		btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);
		setupEventButton();
	}

	//
	private void setupEventButton() {
		btnCreateEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int stringMaxLength, startDay, startMonth, startYear, endDay, endMonth, endYear, startHour, startMinute, endHour, endMinute;
				boolean validDate = false, validTime = false, validTitle = false, validDescription = false;
				
				//check if title max length is under database specs and if empty
				stringMaxLength = (textFieldTitle.getText().toString().length() < 256) ? textFieldTitle.getText().toString().length():256;
				String titleString = textFieldTitle.getText().toString().substring(0, stringMaxLength);
				System.out.println(titleString);
				if (!titleString.isEmpty())
				{
					System.out.println("Title is valid");
					validTitle = true;
				}
				
				//check if description max is under database specs
				stringMaxLength = (textFieldDescription.getText().toString().length() < 256)?textFieldDescription.getText().toString().length():256;
				String descriptionString = textFieldDescription.getText().toString().substring(0, stringMaxLength);
				System.out.println(descriptionString);
				if (!descriptionString.isEmpty())
				{
					System.out.println("Description is valid");
					validDescription = true;
				}
				
				//return privacy setting
				System.out.println(radioButtonOPublic.isChecked());
				System.out.println(radioButtonFPublic.isChecked());
				System.out.println(radioButtonPrivate.isChecked());
				
				//check if start date is after end date
				startDay =datePickerStartDate.getDayOfMonth();
				startMonth = datePickerStartDate.getMonth();
				startYear =datePickerStartDate.getYear();
				endDay =datePickerEndDate.getDayOfMonth();
				endMonth = datePickerEndDate.getMonth();
				endYear =datePickerEndDate.getYear();
						
				System.out.println(startMonth + "/" + startDay + "/" + startYear);
				System.out.println(endMonth + "/" + endDay + "/" + endYear);
				
				if (startYear <= endYear && startMonth <= endMonth && startDay <= endDay)
				{
					System.out.println("Dates are valid");
					validDate = true;
				}
				
				//if date validation passes, check if start time is after end time if same day
				startHour = timePickerStartTime.getCurrentHour();
				startMinute = timePickerStartTime.getCurrentMinute();
				endHour = timePickerEndTime.getCurrentHour();
				endMinute = timePickerEndTime.getCurrentMinute();

				System.out.println(startHour + ":" + startMinute);
				System.out.println(endHour + ":" + endMinute);
				if (startYear == endYear && startMonth == endMonth && startDay == endDay)
				{
					if (startHour <= endHour && startMinute <= endMinute)
					{
						System.out.println("Times are valid");
						validTime = true;
					}
				} else if (validDate == true) {
					System.out.println("Times are valid");
					validTime = true;
				}
				
				
				if (validDate == false || validTime == false || validDescription == false || validTitle == false)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setTitle("Wrong Input");
					alertDialogBuilder.setMessage("Please input valid data");
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				} else {
					//TODO write verified event data to database
					
				}
			}
		});
	}
}
