package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO rewrite this thing
public class AddClassActivity extends Activity {
	private Spinner sTerm;
	private Spinner sCollege;
	private Spinner sCourse;

	private LinearLayout llProgressBar;
	private ListView lvSearchResults;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addclass_activity);
		setResult(RESULT_CANCELED);

		final int id = getIntent().getIntExtra("userID", NULL_USER);

		sTerm = (Spinner)findViewById(R.id.sTerm);
		sCollege = (Spinner)findViewById(R.id.sCollege);
		sCourse = (Spinner)findViewById(R.id.sCourse);

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);
		lvSearchResults = (ListView)findViewById(R.id.lvSearchResults);

		setupTermSpinner();
	}

	private void setupTermSpinner() {
		sTerm.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Term t = (Term)sTerm.getAdapter().getItem(position);
				setupCollegeSpinner(t);
				sCollege.setEnabled(true);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				//...
			}
		});

		// TODO JSON HTTP SYNC REQUESTS?!
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getterms.php", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Term> terms = new ArrayList<Term>();
				if (1 < response.length()) {
					try {
						Term t;
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);

							t = new Term();
							t.term = jObj.getString("term");
							terms.add(t);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(AddClassActivity.this, android.R.layout.simple_spinner_item, terms);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sTerm.setAdapter(adapter);
			}
		});
	}

	private void setupCollegeSpinner(final Term t) {
		sCollege.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				College c = (College)sCollege.getAdapter().getItem(position);
				performSearchQuery(t, c, null);
				setupCourseSpinner(t, c);
				sCourse.setEnabled(true);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				//...
			}
		});

		RequestParams params = new RequestParams();
		params.put("term", t.term);

		// TODO Computer Science (CS) on list
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getmajors.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<College> colleges = new ArrayList<College>();
				if (1 < response.length()) {
					try {
						College c;
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);

							c = new College();
							c.major_long = jObj.getString("major_long");
							c.major_short = jObj.getString("major_short");
							colleges.add(c);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<College> adapter = new ArrayAdapter<College>(AddClassActivity.this, android.R.layout.simple_spinner_item, colleges);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sCollege.setAdapter(adapter);
			}
		});
	}

	private void setupCourseSpinner(final Term term, final College college) {
		sCourse.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Course course = (Course)sCourse.getAdapter().getItem(position);
				if (course.equals(Course.NULL_COURSE)) {
					performSearchQuery(term, college, null);
				} else {
					performSearchQuery(term, college, course);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				//...
			}
		});

		RequestParams params = new RequestParams();
		params.put("major", college.major_short);
		params.put("distinct", "yes");

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getclasses.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Course> courses = new ArrayList<Course>();
				courses.add(Course.NULL_COURSE);
				if (1 < response.length()) {
					try {
						Course c;
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);

							c = new Course();
							c.college = college;
							c.class_num = jObj.getString("class_num");
							courses.add(c);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(AddClassActivity.this, android.R.layout.simple_spinner_item, courses);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sCourse.setAdapter(adapter);
			}
		});
	}

	private void performSearchQuery(Term term, College college, Course course) {
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("major", college.major_short);

		if (course != null) {
			params.put("class_num", course.class_num);
		} else {
			params.put("term", term.term);
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getclasses.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				llProgressBar.setVisibility(View.GONE);

				List<Section> schedule = new ArrayList<Section>();
				if (1 < response.length()) {
					try {
						Section s;
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);

							s = new Section();
							s.class_id = jObj.getInt("class_id");
							s.title = jObj.getString("title");
							s.time_start = jObj.getString("time_start");
							s.time_end = jObj.getString("time_end");
							s.weekdays = jObj.getString("weekdays");
							s.date_start = jObj.getString("date_start");
							s.date_end = jObj.getString("date_end");
							s.instructor = jObj.getString("instructor");
							s.building = jObj.getString("building");
							s.room = jObj.getString("room");
							s.section = jObj.getString("section");
							s.major_short = jObj.getString("major_short");
							s.major_long = jObj.getString("major_long");
							s.class_num = jObj.getString("class_num");
							s.term = jObj.getString("term");
							schedule.add(s);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				SectionSearchAdapter adapter = new SectionSearchAdapter(AddClassActivity.this, schedule);
				lvSearchResults.setAdapter(adapter);
				lvSearchResults.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						LinearLayout llExtendedInfo = (LinearLayout)view.findViewById(R.id.llExtendedInfo);
						llExtendedInfo.setVisibility(llExtendedInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
					}
				});
			}
		});
	}
}
