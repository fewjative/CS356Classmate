package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.content.Context;
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
import edu.csupomona.cs.cs356.classmate.MainActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import java.util.List;

public class FriendRequestsAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	public FriendRequestsAdapter(Context context, List<Friend> requests) {
		super(context, 0, requests);
	}

	private static class ViewHolder {
		final ImageView avatar;
		final TextView tvItemTextUsername;
		final ImageButton btnAccept;
		final ImageButton btnReject;

		ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnAccept, ImageButton btnReject) {
			this.avatar = avatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnAccept = btnAccept;
			this.btnReject = btnReject;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.tab_friends_requests_list_item, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnAccept = (ImageButton)view.findViewById(R.id.btnAccept);
			ImageButton btnReject = (ImageButton)view.findViewById(R.id.btnReject);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnAccept, btnReject));

			tvItemTextUsername.setSelected(true);

			btnAccept.setTag(f);
			btnAccept.setOnClickListener(this);

			btnReject.setTag(f);
			btnReject.setOnClickListener(this);
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
			case R.id.btnAccept:
				acceptInvite(r);
				break;
			case R.id.btnReject:
				rejectInvite(r);
				break;
		}
	}

	public void acceptInvite(final Friend f) {
		String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);
		params.put("user_id", Long.toString(f.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/acceptfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				((MainActivity)getContext()).updateFriendRequestsNum();
			}
		});
	}

	public void rejectInvite(final Friend f) {
		String emailAddress = ((FragmentActivity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("email", emailAddress);
		params.put("user_id", Long.toString(f.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/dismissfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				((MainActivity)getContext()).updateFriendRequestsNum();
			}
		});
	}
}
