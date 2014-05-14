package edu.csupomona.classmate.fragments.addclassevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.College;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.Term;
import edu.csupomona.classmate.abstractions.Course;
import edu.csupomona.classmate.abstractions.User;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddClassTab extends Fragment implements Constants{
	private Spinner sTerm;
	private Spinner sCollege;
	private Spinner sCourse;

	private LinearLayout llProgressBar;
	private ListView lvSearchResults;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.addclassevent_class_fragment_tab_layout, null);
		
		sTerm = (Spinner)root.findViewById(R.id.sTerm);
		sCollege = (Spinner)root.findViewById(R.id.sCollege);
		sCourse = (Spinner)root.findViewById(R.id.sCourse);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		lvSearchResults = (ListView)root.findViewById(R.id.lvSearchResults);

		setupTermSpinner();

		return root;
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
							t = new Term(
								  jObj.getString("term")
							);

							terms.add(t);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(getActivity(), android.R.layout.simple_spinner_item, terms);
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
		params.put("term", t.getTerm());

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
							c = new College(
								jObj.getString("major_short"),
								jObj.getString("major_long")
							);

							colleges.add(c);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<College> adapter = new ArrayAdapter<College>(getActivity(), android.R.layout.simple_spinner_item, colleges);
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
		params.put("major", college.getMajorShort());
		params.put("distinct", "yes");
		params.put("term", term.getTerm());

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
							c = new Course(
								college,
								jObj.getString("class_num")
							);

							courses.add(c);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				sCourse.setAdapter(adapter);
			}
		});
	}

	private void performSearchQuery(Term term, College college, Course course) {
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("major", college.getMajorShort());

		if (course != null) {
			params.put("class_num", course.class_num);
		} else {
			params.put("term", term.getTerm());
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
							s = new Section(
								jObj.getInt("class_id"),
								jObj.getString("title"),
								jObj.getString("time_start"),
								jObj.getString("time_end"),
								jObj.getString("weekdays"),
								jObj.getString("date_start"),
								jObj.getString("date_end"),
								jObj.getString("instructor"),
								jObj.getString("building"),
								jObj.getString("room"),
								jObj.getString("section"),
								jObj.getString("major_short"),
								jObj.getString("major_long"),
								jObj.getString("class_num"),
								jObj.getString("term")
							);

							schedule.add(s);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
				SectionSearchAdapter adapter = new SectionSearchAdapter(getActivity(), USER, schedule);
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