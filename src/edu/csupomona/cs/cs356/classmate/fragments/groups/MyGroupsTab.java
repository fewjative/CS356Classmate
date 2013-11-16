package edu.csupomona.cs.cs356.classmate.fragments.groups;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import static edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment.INTENT_KEY_GROUP;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyGroupsTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.tab_groups_list, null);

		final String email = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		Button btnCreateGroup = (Button)root.findViewById(R.id.btnCreateGroup);
		btnCreateGroup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
				d.setTitle("What would you like to name your group?");

				final EditText input = new EditText(getActivity());
				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				d.setView(input);

				d.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						createGroup(input.getText().toString(), email);
					}
				});

				d.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				d.show();
			}
		});

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("email", email);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getgroups.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Group> groups = new ArrayList<Group>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							groups.add(new Group(
								jObj.getInt("group_id"),
								jObj.getString("title")
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				MyGroupsListAdapter adapter = new MyGroupsListAdapter(getActivity(), groups);
				ListView lvGroupsList = (ListView)root.findViewById(R.id.lvGroupsList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvGroupsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return root;
	}

	private void createGroup(final String groupName, final String email) {
		RequestParams params = new RequestParams();
		params.put("title", groupName);
		params.put("email", email);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/creategroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (response.compareTo("0") == 0) {
					AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
					d.setTitle("Group Exists");
					d.setMessage("There is already a group with this name.");
					d.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

					d.show();
				} else {
					Intent i = new Intent(getActivity(), ManageGroupActivity.class);
					i.putExtra(INTENT_KEY_GROUP, new Group(
						  Integer.parseInt(response),
						  groupName
					));

					startActivity(i);
				}
			}
		});
	}
}
