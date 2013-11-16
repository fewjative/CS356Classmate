package edu.csupomona.cs.cs356.classmate.fragments.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import static edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment.INTENT_KEY_GROUP;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageGroupActivity extends Activity {
	public static final int CODE_ADD_FRIEND = 0x000C;

	private Group group;

	private TextView tvGroupName;
	private ListView lvGroupMembers;
	private LinearLayout llProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_group_activity);

		group = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		Button btnAddMember = (Button)findViewById(R.id.btnAddMember);
		btnAddMember.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ManageGroupActivity.this, AddMemberActivity.class);
				i.putExtra(INTENT_KEY_GROUP, group);
				startActivityForResult(i, CODE_ADD_FRIEND);
			}
		});

		tvGroupName = (TextView)findViewById(R.id.tvGroupName);
		tvGroupName.setText(group.getTitle());

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);

		lvGroupMembers = (ListView)findViewById(R.id.lvGroupMembers);
		refreshMembers();
	}

	private void refreshMembers() {
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("group_id", Integer.toString(group.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getpeopleingroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Friend> people = new ArrayList<Friend>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							people.add(new Friend(
								jObj.getInt("user_id"),
								jObj.getString("username"),
								jObj.getString("email")
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				group.setPeople(people);

				ManageGroupAdapter adapter = new ManageGroupAdapter(ManageGroupActivity.this, group);
				lvGroupMembers.setAdapter(adapter);
				llProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_ADD_FRIEND:
				refreshMembers();
				break;
		}
	}
}