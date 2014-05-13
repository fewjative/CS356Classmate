package edu.csupomona.classmate.fragments.addclassevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.College;
import edu.csupomona.classmate.abstractions.Course;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.Term;
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
	private ListView lvQueryResults;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup ROOT = (ViewGroup)inflater.inflate(R.layout.addclassevent_class_fragment_tab_layout, null);

		TextView tvTerm = (TextView)ROOT.findViewById(R.id.tvTerm);
		tvTerm.setText(getString(R.string.schedule_field, getString(R.string.schedule_term)));

		TextView tvCollege = (TextView)ROOT.findViewById(R.id.tvCollege);
		tvCollege.setText(getString(R.string.schedule_field, getString(R.string.schedule_college)));

		TextView tvCourse = (TextView)ROOT.findViewById(R.id.tvCourse);
		tvCourse.setText(getString(R.string.schedule_field, getString(R.string.schedule_course)));

		sTerm = (Spinner)ROOT.findViewById(R.id.sTerm);
		sCollege = (Spinner)ROOT.findViewById(R.id.sCollege);
		sCourse = (Spinner)ROOT.findViewById(R.id.sCourse);

		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);

		setupTermSpinner();

		return ROOT;
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

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETTERMS, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<Term> terms = new ArrayList<Term>();

				try {
					Term t;
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						t = new Term(
							  jObj.getString(Constants.PHP_PARAM_TERM)
						);

						terms.add(t);
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
		params.put(Constants.PHP_PARAM_TERM, t.getTerm());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETMAJORS, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<College> colleges = new ArrayList<College>();

				try {
					College c;
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						c = new College(
							jObj.getString(Constants.PHP_PARAM_MAJORSHORT),
							jObj.getString(Constants.PHP_PARAM_MAJORLONG)
						);

						colleges.add(c);
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
		params.put(Constants.PHP_PARAM_MAJOR, college.getMajorShort());
		params.put(Constants.PHP_PARAM_DISTINCT, "yes");
		params.put(Constants.PHP_PARAM_TERM, term.getTerm());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETCOURSES, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<Course> courses = new ArrayList<Course>();
				courses.add(Course.NULL_COURSE);

				try {
					Course c;
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						c = new Course(
							college,
							jObj.getString(Constants.PHP_PARAM_CLASSNUM)
						);

						courses.add(c);
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
		params.put(Constants.PHP_PARAM_MAJOR, college.getMajorShort());

		if (course != null) {
			params.put(Constants.PHP_PARAM_CLASSNUM, course.class_num);
		} else {
			params.put(Constants.PHP_PARAM_TERM, term.getTerm());
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETCOURSES, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				llProgressBar.setVisibility(View.GONE);

				List<Section> schedule = new ArrayList<Section>();
				try {
					Section s;
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						s = new Section(
							jObj.getInt(Constants.PHP_PARAM_CLASS_ID),
							jObj.getString(Constants.PHP_PARAM_CLASS_TITLE),
							jObj.getString(Constants.PHP_PARAM_CLASS_STARTTIME),
							jObj.getString(Constants.PHP_PARAM_CLASS_ENDTIME),
							jObj.getString(Constants.PHP_PARAM_CLASS_WEEKDAYS),
							jObj.getString(Constants.PHP_PARAM_CLASS_STARTDATE),
							jObj.getString(Constants.PHP_PARAM_CLASS_ENDDATE),
							jObj.getString(Constants.PHP_PARAM_CLASS_INSTRUCTOR),
							jObj.getString(Constants.PHP_PARAM_CLASS_BUILDING),
							jObj.getString(Constants.PHP_PARAM_CLASS_ROOM),
							jObj.getString(Constants.PHP_PARAM_CLASS_SECTION),
							jObj.getString(Constants.PHP_PARAM_MAJORSHORT),
							jObj.getString(Constants.PHP_PARAM_MAJORLONG),
							jObj.getString(Constants.PHP_PARAM_CLASSNUM),
							jObj.getString(Constants.PHP_PARAM_TERM)
						);

						schedule.add(s);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				SectionSearchAdapter adapter = new SectionSearchAdapter(getActivity(), schedule);
				lvQueryResults.setAdapter(adapter);
				lvQueryResults.setOnItemClickListener(new ListView.OnItemClickListener() {
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