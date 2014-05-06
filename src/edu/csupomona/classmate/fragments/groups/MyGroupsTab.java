package edu.csupomona.classmate.fragments.groups;

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
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.CODE_MANAGEGROUP;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.groups.activities.ManageGroupActivity;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyGroupsTab extends Fragment {
	private User user;
	private View root;
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = (View)inflater.inflate(R.layout.groups_list_tab_layout, null);
		user = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		Button btnCreateGroup = (Button)root.findViewById(R.id.btnCreateGroup);
		btnCreateGroup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
				d.setTitle(R.string.dialog_group_create_title);

				final EditText INPUT = new EditText(getActivity());
				INPUT.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				d.setView(INPUT);

				d.setPositiveButton(getResources().getString(R.string.global_action_okay), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						createGroup(INPUT.getText().toString());
					}
				});

				d.setNegativeButton(getResources().getString(R.string.global_action_cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				d.show();
			}
		});

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		refreshGroups();

		return root;
	}

	private void createGroup(final String groupName) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPTITLE, groupName);
		params.put(Constants.PHP_PARAM_EMAIL, user.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_CREATEGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (response.equals("0")) {
					AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
					d.setTitle(R.string.dialog_group_create_error_title);
					d.setMessage(getActivity().getString(R.string.dialog_group_create_error, groupName));
					d.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

					d.show();
				} else {
					Intent i = new Intent(getActivity(), ManageGroupActivity.class);
					i.putExtra(INTENT_KEY_GROUP, new Group(
						  Long.parseLong(response),
						  groupName
					));

					startActivityForResult(i, CODE_MANAGEGROUP);
				}
			}
		});
	}

	private void refreshGroups() {
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, user.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETGROUPS, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<Group> groups = new ArrayList<Group>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						groups.add(new Group(
							jObj.getInt(Constants.PHP_PARAM_GROUPID),
							jObj.getString(Constants.PHP_PARAM_GROUPTITLE)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				MyGroupsAdapter adapter = new MyGroupsAdapter(getActivity(), user, groups);
				ListView lvGroupsList = (ListView)root.findViewById(R.id.lvGroupsList);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)root.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(getResources().getString(R.string.groups_list_empty, getResources().getString(R.string.groups_join_group), getResources().getString(R.string.groups_create_group)));

					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvGroupsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Constants.CODE_MANAGEGROUP:
				refreshGroups();
				break;
		}
	}
}
