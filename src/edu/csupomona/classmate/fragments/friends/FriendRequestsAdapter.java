package edu.csupomona.classmate.fragments.friends;

import android.content.Context;
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
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class FriendRequestsAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	private final User USER;

	public FriendRequestsAdapter(Context context, User user, List<User> requests) {
		super(context, 0, requests);
		this.USER = user;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvItemTextUsername;
		final ImageButton btnAccept;
		final ImageButton btnCancel;

		ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnAccept, ImageButton btnCancel) {
			this.ivAvatar = avatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnAccept = btnAccept;
			this.btnCancel = btnCancel;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.user_item_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnAccept = (ImageButton)view.findViewById(R.id.btnAccept);
			ImageButton btnCancel = (ImageButton)view.findViewById(R.id.btnCancel);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnAccept, btnCancel));

			tvItemTextUsername.setSelected(true);

			btnAccept.setTag(f);
			btnAccept.setOnClickListener(this);
			btnAccept.setVisibility(View.VISIBLE);

			btnCancel.setTag(f);
			btnCancel.setOnClickListener(this);
			btnCancel.setVisibility(View.VISIBLE);
		//}

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
		int id = v.getId();
		if (id == R.id.btnAccept) {
			acceptInvite(r);
		} else if (id == R.id.btnCancel) {
			rejectInvite(r);
		}
	}

	public void acceptInvite(final User f) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
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
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());
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