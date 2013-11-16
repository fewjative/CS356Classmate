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

public class ManageGroupAdapter extends ArrayAdapter<Friend> implements View.OnClickListener {
	private final Group group;

	public ManageGroupAdapter(Context context, Group group) {
		super(context, 0, group.getPeople());
		this.group = group;
	}

	private static class ViewHolder {
		final ImageView avatar;
		final TextView tvItemTextUsername;
		final ImageButton btnRemoveFromGroup;

		ViewHolder(ImageView avatar, TextView tvItemTextUsername, ImageButton btnRemoveFromGroup) {
			this.avatar = avatar;
			this.tvItemTextUsername = tvItemTextUsername;
			this.btnRemoveFromGroup = btnRemoveFromGroup;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend f = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.groups_manager_list_item, null);

			ImageView ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
			TextView tvItemTextUsername = (TextView)view.findViewById(R.id.tvItemTextUsername);
			ImageButton btnRemoveFromGroup = (ImageButton)view.findViewById(R.id.btnRemoveFromGroup);
			view.setTag(new ViewHolder(ivAvatar, tvItemTextUsername, btnRemoveFromGroup));

			tvItemTextUsername.setSelected(true);

			btnRemoveFromGroup.setTag(f);
			btnRemoveFromGroup.setOnClickListener(this);
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
			case R.id.btnRemoveFromGroup:
				removeGroup(f);
				break;
		}
	}

	public void removeGroup(final Friend member) {
		RequestParams params = new RequestParams();
		params.put("group_id", Integer.toString(group.getID()));
		params.put("email", member.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/removegroup.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(member);
			}
		});
	}
}
