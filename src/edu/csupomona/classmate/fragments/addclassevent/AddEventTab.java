package edu.csupomona.classmate.fragments.addclassevent;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

 public class AddEventTab extends Fragment implements Constants{
	 
	 	final int DATE_DIALOG_ID = 1;
	 	private int start_year, start_month, start_day;
	 	private int end_year, end_month, end_day;
	 	private int start_hour, start_min;
	 	private int end_hour, end_min;
	 	private int FLAG = 0;
	 	private Button setStartDateBtn;
	 	private Button setEndDateBtn;
	 	private Button setStartTimeBtn;
	 	private Button setEndTimeBtn;
	 	private EditText textFieldDescription;
		private EditText textFieldTitle;
		private RadioButton radioButtonOPublic;
		private RadioButton radioButtonFPublic;
//		private RadioButton radioButtonPrivate;
		private Button btnCreateEvent;
		private User USER;
		
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.addclassevent_event_fragment_tab_layout, container, false);
		USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		

		setStartDateBtn = (Button)ROOT.findViewById(R.id.setStartDateBtn);
		setEndDateBtn = (Button)ROOT.findViewById(R.id.setEndDateBtn);
		setStartTimeBtn = (Button)ROOT.findViewById(R.id.setStartTimeBtn);
		setEndTimeBtn = (Button)ROOT.findViewById(R.id.setEndTimeBtn);
		textFieldDescription = (EditText)ROOT.findViewById(R.id.textFieldDescription);
		textFieldTitle = (EditText)ROOT.findViewById(R.id.textFieldTitle);
		radioButtonOPublic = (RadioButton)ROOT.findViewById(R.id.radioButtonOPublic);
		radioButtonFPublic = (RadioButton)ROOT.findViewById(R.id.radioButtonFPublic);
//		radioButtonPrivate = (RadioButton)ROOT.findViewById(R.id.radioButtonPrivate);
		btnCreateEvent = (Button)ROOT.findViewById(R.id.btnCreateEvent);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) ROOT.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		btnCreateEvent.setWidth(width / 2);
		
		OnClickListener listenerStartDate = new OnClickListener() {

			@Override
	        public void onClick(View arg0) {

	            final Calendar c = Calendar.getInstance();
	            start_year = c.get(Calendar.YEAR);
	            start_month = c.get(Calendar.MONTH);
	            start_day = c.get(Calendar.DAY_OF_MONTH);
	            FLAG = 1;
	            DatePickerDialog d = new DatePickerDialog(getActivity(),
	            		 mDateSetListener, start_year, start_month, start_day);
	            d.show();
	        }
	    };
	    
	    OnClickListener listenerEndDate = new OnClickListener() {

			@Override
	        public void onClick(View arg0) {

	            final Calendar c = Calendar.getInstance();
	            end_year = c.get(Calendar.YEAR);
	            end_month = c.get(Calendar.MONTH);
	            end_day = c.get(Calendar.DAY_OF_MONTH);
	            FLAG = 2;
	            DatePickerDialog d = new DatePickerDialog(getActivity(),
	            		 mDateSetListener, end_year, end_month, end_day);
	            d.show();
	        }
	    };
	    
	    OnClickListener listenerStartTime = new OnClickListener() {

			@Override
	        public void onClick(View arg0) {

	            final Calendar c = Calendar.getInstance();
	            start_hour = c.get(Calendar.HOUR);
	            start_min = c.get(Calendar.MINUTE);
	            FLAG = 1;
	            TimePickerDialog t = new TimePickerDialog(getActivity(),
	            		 mTimeSetListener, start_hour, start_min, false);
	            t.show();
	        }
	    };
	    
	    OnClickListener listenerEndTime = new OnClickListener() {

			@Override
	        public void onClick(View arg0) {

	            final Calendar c = Calendar.getInstance();
	            end_hour = c.get(Calendar.HOUR);
	            end_min = c.get(Calendar.MINUTE);
	            FLAG = 2;
	            TimePickerDialog t = new TimePickerDialog(getActivity(),
	            		 mTimeSetListener, end_hour, end_min, false);
	            t.show();
	        }
	    };
	    
	    setStartDateBtn.setOnClickListener(listenerStartDate);
	    setEndDateBtn.setOnClickListener(listenerEndDate);
	    setStartTimeBtn.setOnClickListener(listenerStartTime);
	    setEndTimeBtn.setOnClickListener(listenerEndTime);
	    
		setupEventButton();
		return ROOT;
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

	    public void onDateSet(DatePicker view, int Year,
	            int monthOfYear, int dayOfMonth) {
	        if (FLAG == 1)
	        {
	        	start_year = Year;
	        	start_month = monthOfYear;
	        	start_day = dayOfMonth;
	        }else if (FLAG == 2)
	        	{
		        	end_year = Year;
		        	end_month = monthOfYear;
		        	end_day = dayOfMonth;
	        	}
	        updateDisplay();
	    }
	};
	
	TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

	    public void onTimeSet(TimePicker view, int Hour, int Minute) {
	        if (FLAG == 1)
	        {
	        	start_hour = Hour;
	        	start_min = Minute;
	        }else if (FLAG == 2)
	        	{
		        	end_hour = Hour;
		        	end_min = Minute;
	        	}
	        updateTimeDisplay();
	    }
	};
	
	@SuppressLint("SimpleDateFormat")
	private void updateDisplay() {

	    GregorianCalendar c = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
	    if (FLAG == 1)
	    {
	    	c = new GregorianCalendar(start_year, start_month, start_day);
	    	setStartDateBtn.setText(sdf.format(c.getTime()));
	    }else if (FLAG == 2)
	    {
	    	c = new GregorianCalendar(end_year, end_month, end_day);
	    	setEndDateBtn.setText(sdf.format(c.getTime()));
	    }
	}
	
	private void updateTimeDisplay() {

		String ampm = null;
	    if (FLAG == 1)
	    {
	    	if (start_hour <= 12)
	    	{
	    		ampm = "AM";
	    		setStartTimeBtn.setText(String.format("%d:%d %s", start_hour, start_min, ampm));
	    	}else
	    	{
	    		ampm = "PM";
	    		setStartTimeBtn.setText(String.format("%d:%d %s", (start_hour - 12), start_min, ampm));
	    	}
	    }else if (FLAG == 2)
	    {
	    	if (start_hour <= 12)
	    	{
	    		ampm = "AM";
	    		setEndTimeBtn.setText(String.format("%d:%d %s", end_hour, end_min, ampm));
	    	}else
	    	{
	    		ampm = "PM";
	    		setEndTimeBtn.setText(String.format("%d:%d %s", (end_hour - 12), end_min, ampm));
	    	}
	    }
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		
		switch (id) {
		case DATE_DIALOG_ID:
			final Calendar c = Calendar.getInstance();
			((DatePickerDialog)dialog).updateDate(c.get(Calendar.YEAR),
					c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			break;
		}
	}

	private void setupEventButton() {
		btnCreateEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int stringMaxLength, startDay, startMonth, startYear, endDay, endMonth, endYear, startHour, startMinute, endHour, endMinute;
				boolean validDate = false, validTime = false, validTitle = false, validDescription = false;
				
				//check if title max length is under database specs and if empty
				stringMaxLength = (textFieldTitle.getText().toString().length() < 256) ? textFieldTitle.getText().toString().length() : 256;
				final String titleString = textFieldTitle.getText().toString().substring(0, stringMaxLength);
				System.out.println(titleString);
				if (!titleString.isEmpty())
				{
					System.out.println("Title is valid");
					validTitle = true;
				}
				
				//check if description max is under database specs
				stringMaxLength = (textFieldDescription.getText().toString().length() < 256) ? textFieldDescription.getText().toString().length() : 256;
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
				startDay = start_day;
				startMonth = start_month;
				startYear = start_year;
				endDay = end_day;
				endMonth = end_month;
				endYear = end_year;
			
				//im pretty sure this code doesn't actually work correctly
				//just works for dates within the same month
				if (startYear <= endYear && startMonth <= endMonth && startDay <= endDay)
				{
					System.out.println("Dates are valid");
					validDate = true;
				}
				
				final String startDate =startYear +"-"+startMonth + "-" +startDay;
				final String endDate = endYear + "-" + endMonth+"-"+endDay;
				
				System.out.println(startDate);
				System.out.println(endDate);
				
				
				//if date validation passes, check if start time is after end time if same day
				startHour = start_hour;
				startMinute = start_min;
				endHour = end_hour;
				endMinute = end_min;
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