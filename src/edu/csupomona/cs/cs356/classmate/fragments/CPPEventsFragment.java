package edu.csupomona.cs.cs356.classmate.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import edu.csupomona.cs.cs356.classmate.abstractions.TimeSlot;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CPPEventsFragment extends Fragment {

//http://www.csupomona.edu/events/index.php?type=featured
	private ViewGroup root;

	public CPPEventsFragment()
	{

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = (ViewGroup)inflater.inflate(R.layout.cpp_events_fragment, null);

		return root;
	}

	/*private void setupFreeTime(String response) {
		List<TimeSlot> freetime = new ArrayList<TimeSlot>();
		if (1 < response.length()) {
			try {
				TimeSlot ts;
				JSONObject jObj;
				JSONArray myjsonarray = new JSONArray(response);
				for (int i = 0; i < myjsonarray.length(); i++) {
					jObj = myjsonarray.getJSONObject(i);
					ts = new TimeSlot(
					
						jObj.getString("time_start"),
						jObj.getString("time_end")
						
					);

					freetime.add(ts);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		FreeTimeScheduleAdapter adapter;
		
		adapter = new FreeTimeScheduleAdapter(getActivity(), freetime);

		ListView lvFreeTime = (ListView)llFreeTime.findViewById(R.id.lvFreeTime);
		lvFreeTime.setAdapter(adapter);
	}*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		}
	}

}
