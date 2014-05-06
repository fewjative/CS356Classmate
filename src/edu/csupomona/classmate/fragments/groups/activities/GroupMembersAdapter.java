package edu.csupomona.classmate.fragments.groups.activities;

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
import edu.csupomona.classmate.abstractions.Group;
import edu.csupomona.classmate.abstractions.User;

public class GroupMembersAdapter extends ArrayAdapter<User> implements View.OnClickListener {
	private final Group GROUP;

	public GroupMembersAdapter(Context context, Group group) {
		super(context, 0, group.getUsers());
		this.GROUP = group;
	}

	private static class ViewHolder {
		final ImageView ivAvatar;
		final TextView tvItemTextUsername;
		final ImageButton btnRemoveFromGroup;

		ViewHolder(ImageView ivAvatar, TextView tvItemTextUsername, ImageButton btnRemoveFromGroup) {
			this.ivAvatar = ivAvatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnRemoveFromGroup = btnRemoveFromGroup;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User u = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.group_member_item_layout, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnRemoveFromGroup = (ImageButton)view.findViewById(R.id.btnRemoveFromGroup);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnRemoveFromGroup));

			tvItemTextUsername.setSelected(true);

			btnRemoveFromGroup.setTag(u);
			btnRemoveFromGroup.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (u != null && holder != null) {
			if (holder.tvItemTextUsername != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				SpannableString username = new SpannableString(u.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);

				SpannableString email = new SpannableString(String.format(" (%s)", u.getEmail()));
				email.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, email.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(email);

				holder.tvItemTextUsername.setText(builder);
			}

			if (holder.ivAvatar != null) {
				holder.ivAvatar.setVisibility(View.VISIBLE);
				u.loadAvatar(holder.ivAvatar);
			}
		}

		return view;
	}

	public void onClick(View v) {
		User u = (User)v.getTag();
		switch (v.getId()) {
			case R.id.btnRemoveFromGroup:
				removeGroup(u);
				break;
		}
	}

	public void removeGroup(final User member) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_GROUPID, Long.toString(GROUP.getID()));
		params.put(Constants.PHP_PARAM_EMAIL, member.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVEGROUP, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(member);
			}
		});
	}
}
