package edu.csupomona.classmate.fragments.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.CODE_MANAGEGROUP;
import static edu.csupomona.classmate.Constants.INTENT_KEY_GROUP;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.groups.activities.ManageGroupActivity;
import java.util.List;

public class MyGroupsAdapter extends ArrayAdapter<Group> implements View.OnClickListener {
	private final User USER;

	public MyGroupsAdapter(Context context, User user, List<Group> groups) {
		super(context, 0, groups);
		this.USER = user;
	}

	private static class ViewHolder {
		final TextView tvItemText;
		final ImageButton btnViewDetails;
		final ImageButton btnCancel;

		ViewHolder(TextView tvItemText, ImageButton btnViewDetails, ImageButton btnCancel) {
			this.tvItemText = tvItemText;
			this.btnViewDetails = btnViewDetails;
			this.btnCancel = btnCancel;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group g = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.group_item_layout, null);

			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnViewDetails = (ImageButton)view.findViewById(R.id.btnViewDetails);
			ImageButton btnCancel = (ImageButton)view.findViewById(R.id.btnCancel);
			view.setTag(new ViewHolder(tvItemText, btnViewDetails, btnCancel));

			tvItemText.setSelected(true);

			btnViewDetails.setTag(g);
			btnViewDetails.setOnClickListener(this);
			btnViewDetails.setVisibility(View.VISIBLE);

			btnCancel.setTag(g);
			btnCancel.setOnClickListener(this);
			btnCancel.setVisibility(View.VISIBLE);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (g != null && holder != null) {
			if (holder.tvItemText != null) {
				holder.tvItemText.setText(g.getName());
			}
		}

		return view;
	}

	public void onClick(View v) {
		Group g = (Group)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewDetails:
				viewDetails(g);
				break;
			case R.id.btnCancel:
				removeGroup(g);
				break;
		}
	}

	public void viewDetails(Group g) {
		Intent i = new Intent(((FragmentActivity)getContext()), ManageGroupActivity.class);
		i.putExtra(INTENT_KEY_GROUP, g);
		((FragmentActivity)getContext()).startActivityForResult(i, CODE_MANAGEGROUP);
	}

	public void removeGroup(final Group g) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(g.getID()));
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVEGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(g);
				AlertDialog.Builder d = new AlertDialog.Builder(getContext());
				d.setTitle(R.string.dialog_group_remove_title);
				d.setMessage(getContext().getString(R.string.dialog_group_remove, g.getName()));
				d.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				d.show();
			}
		});
	}
}
