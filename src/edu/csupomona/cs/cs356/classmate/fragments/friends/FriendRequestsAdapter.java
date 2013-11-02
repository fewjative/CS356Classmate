package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.app.Activity;
import android.content.Context;
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

public class FriendRequestsAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	public FriendRequestsAdapter(Context context, List<Friend> requests) {
		super(context, 0, requests);
	}

	private static class ViewHolder {
		final ImageView avatar;
		final TextView textHolder;
		final ImageButton btnAccept;
		final ImageButton btnReject;

		ViewHolder(ImageView avatar, TextView textHolder, ImageButton btnAccept, ImageButton btnReject) {
			this.avatar = avatar;
			this.textHolder = textHolder;
			this.btnAccept = btnAccept;
			this.btnReject = btnReject;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend r = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.tab_friends_requests_list_item, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnAccept = (ImageButton)view.findViewById(R.id.btnAccept);
			ImageButton btnReject = (ImageButton)view.findViewById(R.id.btnReject);
			view.setTag(new ViewHolder(ivAvatar, tvItemText, btnAccept, btnReject));

			btnAccept.setTag(r);
			btnAccept.setOnClickListener(this);

			btnReject.setTag(r);
			btnReject.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (r != null && holder != null) {
			if (holder.textHolder != null) {
				holder.textHolder.setText(r.username);
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
			case R.id.btnAccept:
				acceptInvite(r);
				break;
			case R.id.btnReject:
				rejectInvite(r);
				break;
		}
	}

	public void acceptInvite(final Friend r) {
		String userName = ((Activity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("user_id", Integer.toString(r.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/acceptfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(r);
			}
		});
	}

	public void rejectInvite(final Friend r) {
		String userName = ((Activity)getContext()).getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("user_id", Integer.toString(r.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/dismissfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(r);
			}
		});
	}
}
