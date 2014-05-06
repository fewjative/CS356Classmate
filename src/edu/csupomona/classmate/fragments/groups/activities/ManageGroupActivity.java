package edu.csupomona.classmate.fragments.groups.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageGroupActivity extends Activity {
	private Group group;

	private TextView tvGroupName;
	private ListView lvGroupMembers;
	private LinearLayout llProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_group_activity_layout);

		group = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		Button btnAddMember = (Button)findViewById(R.id.btnAddMember);
		btnAddMember.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(ManageGroupActivity.this, "Not implemented yet!", Toast.LENGTH_LONG).show();

				// TODO: Implement
				//Intent i = new Intent(ManageGroupActivity.this, AddMemberActivity.class);
				//i.putExtra(INTENT_KEY_GROUP, group);
				//startActivityForResult(i, CODE_ADD_FRIEND);
			}
		});

		Button btnEmailGroup = (Button)findViewById(R.id.btnEmailGroup);
		btnEmailGroup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ManageGroupActivity.this, EmailGroupActivity.class);
				i.putExtra(INTENT_KEY_GROUP, group);
				startActivity(i);
			}
		});

		tvGroupName = (TextView)findViewById(R.id.tvGroupName);
		tvGroupName.setText(group.getName());

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);

		lvGroupMembers = (ListView)findViewById(R.id.lvGroupMembers);
		refreshMembers();
	}

	private void refreshMembers() {
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(group.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETGROUP, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<User> people = new ArrayList<User>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						people.add(new User(
							jObj.getLong(Constants.PHP_PARAM_USERID),
							jObj.getString(Constants.PHP_PARAM_NAME),
							jObj.getString(Constants.PHP_PARAM_EMAIL)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				group.setUsers(people);

				GroupMembersAdapter adapter = new GroupMembersAdapter(ManageGroupActivity.this, group);
				lvGroupMembers.setAdapter(adapter);
				llProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Constants.CODE_ADDMEMBER:
				refreshMembers();
				break;
		}
	}
}