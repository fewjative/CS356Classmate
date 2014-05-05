package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import java.util.List;

public class AddFriendAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	public AddFriendAdapter(Context context, List<Friend> friends) {
		super(context, 0, friends);
	}

	private static class ViewHolder {
		final ImageView avatar;
		final TextView tvItemTextUsername;
		final ImageButton btnSendRequest;

		ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnSendRequest) {
			this.avatar = avatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnSendRequest = btnSendRequest;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.tab_friends_add_list_item, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnSendRequest = (ImageButton)view.findViewById(R.id.btnSendRequest);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnSendRequest));

			tvItemTextUsername.setSelected(true);

			btnSendRequest.setTag(f);
			btnSendRequest.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (f != null && holder != null) {
			if (holder.tvItemTextUsername != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString username = new SpannableString(f.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);

				SpannableString email = new SpannableString(String.format(" (%s)", f.getEmail()));
				email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(email);

				holder.tvItemTextUsername.setText(builder);
			}

			if (holder.avatar != null) {
				holder.avatar.setVisibility(View.VISIBLE);
				holder.avatar.setImageResource(f.getAvatar());
			}
		}

		return view;
	}

	public void onClick(View v) {
		Friend r = (Friend)v.getTag();
		switch (v.getId()) {
			case R.id.btnSendRequest:
				sendRequest(r);
				break;
		}
	}

	public void sendRequest(final Friend f) {
		String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);
		params.put("friend", f.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/addfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				AlertDialog d = new AlertDialog.Builder(getContext()).create();
				d.setTitle("Request Sent");
				d.setMessage(String.format("A request has been dispatched to %s.", f.getUsername()));
				d.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(R.string.okay), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				d.show();
			}
		});
	}

	public void removeFriend(final Friend f) {
		AlertDialog d = new AlertDialog.Builder(((FragmentActivity)getContext())).create();
		d.setTitle(R.string.removeTitle);
		d.setMessage(getContext().getResources().getString(R.string.removeConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

				RequestParams params = new RequestParams();
				params.put("email", emailAddress);
				params.put("user_id", Long.toString(f.getID()));
				params.put("version", "1");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(f);
					}

				});

			}
		});

		d.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

				RequestParams params = new RequestParams();
				params.put("email", emailAddress);
				params.put("user_id", Long.toString(f.getID()));
				params.put("version", "2");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						remove(f);
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
