package edu.csupomona.cs.cs356.classmate.fragments.groups;

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
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import java.util.List;

public class AddMemberAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	private final Group group;

	public AddMemberAdapter(Context context, Group group, List<Friend> friends) {
		super(context, 0, friends);
		this.group = group;
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
		Friend f = (Friend)v.getTag();
		switch (v.getId()) {
			case R.id.btnSendRequest:
				joinGroup(f);
				break;
		}
	}

	public void joinGroup(final Friend f) {
		RequestParams params = new RequestParams();
		params.put("group_id", Integer.toString(group.getID()));
		params.put("email", f.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/addtogroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(f);
				group.getPeople().add(f);
			}
		});
	}
}
