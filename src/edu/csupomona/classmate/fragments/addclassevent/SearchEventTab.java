package edu.csupomona.classmate.fragments.addclassevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.abstractions.Event;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.FriendRequestsAdapter;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.utils.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchEventTab extends Fragment implements Constants{
	private EditText etEventName;
	private ListView lvSearchResults;
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.addclassevent_search_event_fragment_tab_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		lvSearchResults = (ListView)root.findViewById(R.id.lvSearchResults);

		etEventName = (EditText)root.findViewById(R.id.etEventName);
		etEventName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put("search", e.toString());
				params.put("user_id", Long.toString(USER.getID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/searchevents.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						List<Event> search_results = new ArrayList<Event>();
						if (1 < response.length()) {
							try {
								JSONObject jObj;
								JSONArray myjsonarray = new JSONArray(response);
								for (int i = 0; i < myjsonarray.length(); i++) {
									jObj = myjsonarray.getJSONObject(i);
									search_results.add(new Event(jObj.getInt("event_id"),jObj.getString("title"),jObj.getString("description"),jObj.getString("time_start"),jObj.getString("time_end"),jObj.getString("date_start"),jObj.getString("date_end"),jObj.getString("weekdays"),jObj.getString("fpublic"),jObj.getString("opublic"), jObj.getString("isprivate")));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						SearchEventsAdapter adapter = new SearchEventsAdapter(getActivity(), USER, search_results,etEventName);
						lvSearchResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});

		etEventName.requestFocus();
		etEventName.setFocusableInTouchMode(true);
		etEventName.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etEventName.requestFocusFromTouch();
				return false;
			}

		});

		return root;
	}
}