package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.Activity;
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

// TODO rewrite this thing
public class AddEventActivity extends Activity {
	private EditText textFieldDescription;
	private EditText textFieldTitle;
	private CheckBox checkBoxPrivate;
	private TimePicker timePickerEvent;
	private DatePicker datePickerEvent;
	private Button btnCreateEvent;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent_activity);
		setResult(RESULT_CANCELED);

		final int id = getIntent().getIntExtra("userID", NULL_USER);

		textFieldDescription = (EditText)findViewById(R.id.textFieldDescription);
		textFieldTitle = (EditText)findViewById(R.id.textFieldTitle);
		checkBoxPrivate = (CheckBox)findViewById(R.id.checkBoxPrivate);
		timePickerEvent = (TimePicker)findViewById(R.id.timePickerEvent);
		datePickerEvent = (DatePicker)findViewById(R.id.datePickerEvent);
		btnCreateEvent = (Button)findViewById(R.id.btnCreateEvent);
		
		
		setupEventButton();
	}

	private void setupEventButton() {
		btnCreateEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO write to database and check valid input

			
			}
		});
	}
}
