package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.List;

public class FriendListAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	public FriendListAdapter(Context context, List<Friend> friends) {
		super(context, 0, friends);
	}

	private static class ViewHolder {
		final ImageView avatar;
		final TextView textHolder;
		final ImageButton btnViewSchedule;
		final ImageButton btnRemove;

		ViewHolder(ImageView avatar, TextView textHolder, ImageButton btnViewSchedule, ImageButton btnRemove) {
			this.avatar = avatar;
			this.textHolder = textHolder;
			this.btnViewSchedule = btnViewSchedule;
			this.btnRemove = btnRemove;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend r = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.tab_friends_list_list_item, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnViewSchedule = (ImageButton)view.findViewById(R.id.btnViewSchedule);
			ImageButton btnRemove = (ImageButton)view.findViewById(R.id.btnRemove);
			view.setTag(new ViewHolder(ivAvatar, tvItemText, btnViewSchedule, btnRemove));

			btnViewSchedule.setTag(r);
			btnViewSchedule.setOnClickListener(this);

			btnRemove.setTag(r);
			btnRemove.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (r != null && holder != null) {
			if (holder.textHolder != null) {
				holder.textHolder.setText(String.format("%s (%s)", r.username, r.emailAddress));
			}

			if (holder.avatar != null) {
				holder.avatar.setVisibility(View.VISIBLE);
				holder.avatar.setImageResource(r.avatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		Friend r = (Friend)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewSchedule:
				viewSchedule(r);
				break;
			case R.id.btnRemove:
				removeFriend(r);
				break;
		}
	}

	public void viewSchedule(final Friend r) {
		String emailAddress = ((Activity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);
		params.put("user_id", Integer.toString(r.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/acceptfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(r);
			}
		});
	}

	public void removeFriend(final Friend r) {
		AlertDialog d = new AlertDialog.Builder(((Activity)getContext())).create();
		d.setTitle(R.string.removeTitle);
		d.setMessage(getContext().getResources().getString(R.string.removeConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String emailAddress = ((Activity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

				RequestParams params = new RequestParams();
				params.put("email", emailAddress);
				params.put("user_id", Integer.toString(r.getID()));
				params.put("version", "1");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(r);
					}

				});

			}
		});

		d.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String emailAddress = ((Activity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

				RequestParams params = new RequestParams();
				params.put("email", emailAddress);
				params.put("user_id", Integer.toString(r.getID()));
				params.put("version", "2");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(r);
					}
				});
			}
		});

		d.setButton(DialogInterface.BUTTON_NEUTRAL, getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		d.show();
	}
}