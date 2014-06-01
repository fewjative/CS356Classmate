package edu.csupomona.classmate.fragments.schedule;

import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class FreeTimeTab extends Fragment implements Constants {

	private User user;
	private ViewGroup root;
	private EditText etScheduleName;
	private Button btnAddSchedule;
	private LinearLayout llAddClass;
	private LinearLayout llNoClass;
	private LinearLayout llSchedule;
	private LinearLayout llNoSchedule;
	private LinearLayout llNoFriendSchedule;
	private LinearLayout llProgressBar;

	private final User VIEWER;

	public FreeTimeTab()
	{
		this.VIEWER = null;
	}
	
	public FreeTimeTab(User viewer) {
		this.VIEWER = viewer;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.freetime_fragment, null);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfriends.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<User> friends = new ArrayList<User>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							friends.add(new User(jObj.getInt("friend_id"), jObj.getString("fusername"), jObj.getString("femail")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FreeTimeTabAdapter adapter = new FreeTimeTabAdapter(getActivity(), friends);
				ListView lvFriendsList = (ListView)root.findViewById(R.id.lvFriendsList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvFriendsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return root;
	}
}
