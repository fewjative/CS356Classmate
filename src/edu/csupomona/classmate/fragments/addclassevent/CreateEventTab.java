package edu.csupomona.classmate.fragments.addclassevent;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 public class CreateEventTab extends Fragment implements Constants{
	 
		private EditText textFieldDescription;
		private EditText textFieldTitle;
		private TextView textViewValid;
		private RadioButton radioButtonOPublic;
		private RadioButton radioButtonFPublic;
		private RadioButton radioButtonPrivate;
		private TimePicker timePickerStartTime, timePickerEndTime;
		private DatePicker datePickerStartDate, datePickerEndDate;
		private Button btnCreateEvent;
		
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.addclassevent_create_fragment_tab_layout, container, false);
		

		textFieldDescription = (EditText)ROOT.findViewById(R.id.textFieldDescription);
		textFieldTitle = (EditText)ROOT.findViewById(R.id.textFieldTitle);
		radioButtonOPublic = (RadioButton)ROOT.findViewById(R.id.radioButtonOPublic);
		radioButtonFPublic = (RadioButton)ROOT.findViewById(R.id.radioButtonFPublic);
		radioButtonPrivate = (RadioButton)ROOT.findViewById(R.id.radioButtonPrivate);
		timePickerStartTime = (TimePicker)ROOT.findViewById(R.id.timePickerStartTime);
		timePickerEndTime = (TimePicker)ROOT.findViewById(R.id.timePickerEndTime);
		datePickerStartDate = (DatePicker)ROOT.findViewById(R.id.datePickerStartDate);
		datePickerEndDate = (DatePicker)ROOT.findViewById(R.id.datePickerEndDate);
		btnCreateEvent = (Button)ROOT.findViewById(R.id.btnCreateEvent);
		textViewValid = (TextView)ROOT.findViewById(R.id.textViewValid);
		setupEventButton();
		return ROOT;
	}
	
	private void setupEventButton() {
		btnCreateEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int stringMaxLength, startDay, startMonth, startYear, endDay, endMonth, endYear, startHour, startMinute, endHour, endMinute;
				boolean validDate = false, validTime = false, validTitle = false, validDescription = false;
				
				//check if title max length is under database specs and if empty
				stringMaxLength = (textFieldTitle.getText().toString().length() < 256) ? textFieldTitle.getText().toString().length():256;
				final String titleString = textFieldTitle.getText().toString().substring(0, stringMaxLength);
				System.out.println(titleString);
				if (!titleString.isEmpty())
				{
					System.out.println("Title is valid");
					validTitle = true;
				}
				
				//check if description max is under database specs
				stringMaxLength = (textFieldDescription.getText().toString().length() < 256)?textFieldDescription.getText().toString().length():256;
				final String descriptionString = textFieldDescription.getText().toString().substring(0, stringMaxLength);
				System.out.println(descriptionString);
				if (!descriptionString.isEmpty())
				{
					System.out.println("Description is valid");
					validDescription = true;
				}
				
				//return privacy setting
				String case1 = "0", case2 = "0", case3 = "0";
				if (radioButtonOPublic.isChecked())
				{
					case1 = "1";
				} else if (radioButtonFPublic.isChecked()) {
					case2 = "1";
				} else {
					case3 = "1";
				}
				final String oPublic = case1, fPublic = case2, Private = case3;
				System.out.println(oPublic);
				System.out.println(fPublic);
				System.out.println(Private);
				
				
				//check if start date is after end date
				startDay = datePickerStartDate.getDayOfMonth();
				startMonth = datePickerStartDate.getMonth();
				startYear = datePickerStartDate.getYear();
				endDay = datePickerEndDate.getDayOfMonth();
				endMonth = datePickerEndDate.getMonth();
				endYear = datePickerEndDate.getYear();
				final String startDate = startDay + "-" + startMonth + "-" + startYear;
				final String endDate = startDay + "-" + endMonth + "-" + endYear;
				
				System.out.println(startDate);
				System.out.println(endDate);
				
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
				final String startTime = startHour + ":" + startMinute;
				final String endTime = endHour + ":" + endMinute;

				System.out.println(startTime);
				System.out.println(endTime);
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
					textViewValid.setVisibility(View.VISIBLE);
					
				} else {
					
					textViewValid.setVisibility(View.INVISIBLE);
					
					//TODO write verified event data to database
					AsyncHttpClient writeEventClient = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					List<NameValuePair> params1 = new ArrayList<NameValuePair>();
					params1.add(new BasicNameValuePair("title", titleString));
					params1.add(new BasicNameValuePair("description", descriptionString));
					params1.add(new BasicNameValuePair("time_start", startTime));
					params1.add(new BasicNameValuePair("time_end", endTime));
					params1.add(new BasicNameValuePair("date_start", startDate));
					params1.add(new BasicNameValuePair("date_end", endDate));
					params1.add(new BasicNameValuePair("weekdays", "N/A"));
					params1.add(new BasicNameValuePair("fpublic", fPublic));
					params1.add(new BasicNameValuePair("opublic", oPublic));
					params1.add(new BasicNameValuePair("private", Private));
					AsyncHttpClient client = new AsyncHttpClient();
					client.get("http://www.lol-fc.com/classmate/getevents.php", params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {

							
							
						}
					});
					
				}
			}
		});
	}
}
