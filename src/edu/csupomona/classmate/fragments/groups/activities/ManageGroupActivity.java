package edu.csupomona.classmate.fragments.groups.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.CODE_ADDMEMBER;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageGroupActivity extends Activity {
	private Group group;

	private ListView lvQueryResults;
	private LinearLayout llProgressBar;

	private GroupMembersAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_group_activity_layout);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		group = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		Button btnAddMember = (Button)findViewById(R.id.btnAddMember);
		btnAddMember.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ManageGroupActivity.this, AddMemberActivity.class);
				i.putExtra(INTENT_KEY_GROUP, group);
				startActivityForResult(i, CODE_ADDMEMBER);
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

		TextView tvGroupName = (TextView)findViewById(R.id.tvGroupName);
		tvGroupName.setText(group.getName());

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);

		lvQueryResults = (ListView)findViewById(R.id.lvQueryResults);
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
				List<User> people = new LinkedList<User>();

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

				adapter = new GroupMembersAdapter(ManageGroupActivity.this, group);
				lvQueryResults.setAdapter(adapter);
				llProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_ADDMEMBER:
				if (resultCode == RESULT_OK) {
					User added = data.getParcelableExtra(Constants.INTENT_KEY_USER);
					adapter.add(added);
					//refreshMembers();
				}

				break;
		}
	}
}