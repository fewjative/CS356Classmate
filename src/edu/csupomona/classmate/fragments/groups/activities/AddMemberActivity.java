package edu.csupomona.classmate.fragments.groups.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddMemberActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_member_activity_layout);

		final Group GROUP = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		final LinearLayout llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);
		final ListView lvSearchResults = (ListView)findViewById(R.id.lvSearchResults);

		final EditText etFriendName = (EditText)findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put(Constants.PHP_PARAM_SEARCH, e.toString());
				params.put(Constants.PHP_PARAM_GROUPID, Long.toString(GROUP.getID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_SEARCHUSERS, params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsona) {
						List<User> search_results = new ArrayList<User>();

						try {
							JSONObject jObj;
							for (int i = 0; i < jsona.length(); i++) {
								jObj = jsona.getJSONObject(i);
								search_results.add(new User(
									jObj.getLong(Constants.PHP_PARAM_USERID),
									jObj.getString(Constants.PHP_PARAM_NAME),
									jObj.getString(Constants.PHP_PARAM_EMAIL)
								));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						AddMemberAdapter adapter = new AddMemberAdapter(AddMemberActivity.this, GROUP, search_results, etFriendName.getWindowToken());
						lvSearchResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});
	}
}
