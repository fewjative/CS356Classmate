package edu.csupomona.cs.cs356.classmate.fragments.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import java.util.List;

public class AddGroupAdapter extends ArrayAdapter<Group> implements View.OnClickListener {
	public AddGroupAdapter(Context context, List<Group> groups) {
		super(context, 0, groups);
	}

	private static class ViewHolder {
		final TextView tvItemText;
		final ImageButton btnJoinGroup;

		ViewHolder(TextView tvItemText, ImageButton btnJoinGroup) {
			this.tvItemText = tvItemText;
			this.btnJoinGroup = btnJoinGroup;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group g = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.tab_groups_add_list_item, null);

			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnJoinGroup = (ImageButton)view.findViewById(R.id.btnJoinGroup);
			view.setTag(new ViewHolder(tvItemText, btnJoinGroup));

			tvItemText.setSelected(true);

			btnJoinGroup.setTag(g);
			btnJoinGroup.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (g != null && holder != null) {
			if (holder.tvItemText != null) {
				holder.tvItemText.setText(g.getTitle());
			}
		}

		return view;
	}

	public void onClick(View v) {
		Group g = (Group)v.getTag();
		switch (v.getId()) {
			case R.id.btnJoinGroup:
				joinGroup(g);
				break;
		}
	}

	public void joinGroup(final Group g) {
		String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("group_id", Integer.toString(g.getID()));
		params.put("email", emailAddress);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/addtogroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(g);
				AlertDialog d = new AlertDialog.Builder(getContext()).create();
				d.setTitle("Group Joined");
				d.setMessage(String.format("You have successfully joined %s.", g.getTitle()));
				d.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(R.string.okay), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				d.show();
			}
		});
	}
}
