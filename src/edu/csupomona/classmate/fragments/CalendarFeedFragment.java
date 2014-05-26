package edu.csupomona.classmate.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.CalendarEvent;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalendarFeedFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.http_query_listview_layout, null);

		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETCALENDARFEED, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<CalendarEvent> calendarEvents = new LinkedList<CalendarEvent>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						calendarEvents.add(new CalendarEvent(
							jObj.getString(Constants.PHP_PARAM_STARTTIME),
							jObj.getString(Constants.PHP_PARAM_TITLE),
							jObj.getString(Constants.PHP_PARAM_DESC),
							jObj.getString(Constants.PHP_PARAM_IMAGEURL),
							jObj.getString(Constants.PHP_PARAM_ARTICLEURL)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				final CalendarFeedAdapter adapter = new CalendarFeedAdapter(getActivity(), calendarEvents);
				ListView lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(R.string.calendar_events_feed_empty);

					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvQueryResults.setAdapter(adapter);
					lvQueryResults.setDivider(null);
					lvQueryResults.setBackgroundResource(R.color.white_smoke);
					lvQueryResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							CalendarEvent event = adapter.getItem(position);
							AlertDialog b = new AlertDialog.Builder(CalendarFeedFragment.this.getActivity()).create();
							b.setTitle(event.getTitle());
							b.setMessage(event.getDesc());
							b.setCanceledOnTouchOutside(true);
							b.show();
						}
					});
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}
