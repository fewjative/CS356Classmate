package edu.csupomona.cs.cs356.classmate.fragment.friends;

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
import edu.csupomona.cs.cs356.classmate.Constants;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.User;
import java.util.List;

public class FriendRequestsListAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	public FriendRequestsListAdapter(Context context, List<User> requests) {
		super(context, 0, requests);
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvItemTextUsername;
		final ImageButton btnAccept;
		final ImageButton btnReject;

		ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnAccept, ImageButton btnReject) {
			this.ivAvatar = avatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnAccept = btnAccept;
			this.btnReject = btnReject;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.friend_requests_tab_item_layout, null);

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

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				f.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		User r = (User)v.getTag();
		switch (v.getId()) {
			case R.id.btnAccept:
				acceptInvite(r);
				break;
			case R.id.btnReject:
				rejectInvite(r);
				break;
		}
	}

	public void acceptInvite(final User f) {
		User user = ((FragmentActivity)getContext()).getIntent().getParcelableExtra(INTENT_KEY_USER);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, user.getEmail());
		params.put(Constants.PHP_PARAM_USERID, Long.toString(f.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_ACCEPTFRIEND, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				// TODO uncomment
				//((MainActivity)getContext()).updateFriendRequestsNum();
			}
		});
	}

	public void rejectInvite(final User f) {
		User user = ((FragmentActivity)getContext()).getIntent().getParcelableExtra(INTENT_KEY_USER);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, user.getEmail());
		params.put(Constants.PHP_PARAM_USERID, Long.toString(f.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_DISMISSFRIEND, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				// TODO uncomment
				//((MainActivity)getContext()).updateFriendRequestsNum();
			}
		});
	}
}