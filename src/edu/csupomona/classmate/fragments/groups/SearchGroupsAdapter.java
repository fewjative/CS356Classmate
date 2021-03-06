package edu.csupomona.classmate.fragments.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class SearchGroupsAdapter extends ArrayAdapter<Group> implements View.OnClickListener {
	private final User USER;

	private boolean lock;

	public SearchGroupsAdapter(Context context, User user, List<Group> groups) {
		super(context, 0, groups);
		this.USER = user;
	}

	private static class ViewHolder {
		final TextView tvItemText;
		final ImageButton btnAdd;

		ViewHolder(TextView tvItemText, ImageButton btnAdd) {
			this.tvItemText = tvItemText;
			this.btnAdd = btnAdd;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group g = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.group_item_layout, null);

			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnAdd = (ImageButton)view.findViewById(R.id.btnAdd);
			view.setTag(new ViewHolder(tvItemText, btnAdd));

			tvItemText.setSelected(true);

			btnAdd.setTag(g);
			btnAdd.setOnClickListener(this);
			btnAdd.setVisibility(View.VISIBLE);
		//}

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
		int id = v.getId();
		if (id == R.id.btnAdd) {
			if (lock) {
				return;
			}
			lock = true;
			joinGroup(g);
		}
	}

	public void joinGroup(final Group g) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(g.getID()));
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_JOINGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(g);
				lock = false;

				AlertDialog.Builder joinedDialog = new AlertDialog.Builder(getContext());
				joinedDialog.setTitle(R.string.dialog_group_join_title);
				joinedDialog.setMessage(getContext().getString(R.string.dialog_group_join, g.getName()));
				joinedDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				joinedDialog.show();
			}
		});
	}
}