package edu.csupomona.classmate.fragments.addclassevent;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

 public class AddEventTab extends Fragment implements Constants{
	 
		private EditText textFieldDescription;
		private EditText textFieldTitle;
		private RadioButton radioButtonOPublic;
		private RadioButton radioButtonFPublic;
		private RadioButton radioButtonPrivate;
		private TimePicker timePickerStartTime, timePickerEndTime;
		private DatePicker datePickerStartDate, datePickerEndDate;
		private Button btnCreateEvent;
		private User USER;
		
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.addclassevent_event_fragment_tab_layout, container, false);
		USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		

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
					//textViewValid.setVisibility(View.VISIBLE);
					AlertDialog.Builder invalidDialog = new AlertDialog.Builder(getActivity());
					invalidDialog.setTitle(R.string.invalid_event_header_hint);
					if (validTitle == false){invalidDialog.setMessage(R.string.invalid_event_title_hint);}
					else if (validDescription == false){invalidDialog.setMessage(R.string.invalid_event_description_hint);}
					else if (validDate == false){invalidDialog.setMessage(R.string.invalid_event_dates_hint);}
					else if (validTime == false){invalidDialog.setMessage(R.string.invalid_event_times_hint);}
					AlertDialog dialog = invalidDialog.create();
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				} else {
					
					RequestParams params = new RequestParams();
					params.put("user_id", Long.toString(USER.getID()));
					params.put("title", titleString);
					params.put("description", descriptionString);
					params.put("time_start", startTime);
					params.put("time_end", endTime);
					params.put("date_start", startDate);
					params.put("date_end", endDate);
					params.put("weekdays", "N/A");
					params.put("fpublic", fPublic);
					params.put("opublic", oPublic);
					params.put("isprivate", Private);
					AsyncHttpClient client = new AsyncHttpClient();
					
					final ProgressDialog loadingDialog = new ProgressDialog(getActivity());
					loadingDialog.setTitle("Creating Event");
					loadingDialog.setMessage("Event is being created as we speak");
					loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					loadingDialog.show();
					
					client.get("http://www.lol-fc.com/classmate/2/createevent.php", params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							System.out.println("response: " + response);
							if (!loadingDialog.isShowing()) {
								return;
							}

							loadingDialog.dismiss();

							int id;
							try {
								id = Integer.parseInt(response);
							} catch (NumberFormatException e) {
								id = NO_USER;
							}

							if (id <= NO_USER) {
								AlertDialog d = new AlertDialog.Builder(getActivity()).create();
								d.setTitle("Error With Event Creation");
								d.setMessage("We could not create the event as this time.");
								d.setIcon(android.R.drawable.ic_dialog_alert);
								d.setOnCancelListener(new DialogInterface.OnCancelListener() {
									public void onCancel(DialogInterface dialog) {
	                                                  // setResult(0);
										//finish();
									}
								});

								d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
	                                                        //setResult(RESULT_CANCELED);
										//finish();
									}
								});

								d.show();
								return;
							} else {
								AlertDialog d = new AlertDialog.Builder(getActivity()).create();
								d.setTitle("Success!");
								d.setMessage("The event has been created!");
								d.setIcon(android.R.drawable.ic_dialog_alert);
								d.setOnCancelListener(new DialogInterface.OnCancelListener() {
									public void onCancel(DialogInterface dialog) {
	                                                  // setResult(0);
										//finish();
									}
								});

								d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
	                                                        //setResult(RESULT_CANCELED);
										//finish();
									}
								});

								d.show();
								//set fields to be empty
								return;
							}
								
						}
					});
					
				}
			}
		});
	}
}