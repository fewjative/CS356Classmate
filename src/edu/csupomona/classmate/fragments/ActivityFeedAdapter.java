package edu.csupomona.classmate.fragments;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.ActivityFeedItem;
import edu.csupomona.classmate.abstractions.ClassAddedEvent;
import edu.csupomona.classmate.abstractions.FriendAddedEvent;
import java.util.List;

public class ActivityFeedAdapter extends ArrayAdapter<ActivityFeedItem> {
	public ActivityFeedAdapter(Context context, List<ActivityFeedItem> activityFeedItems) {
		super(context, 0, activityFeedItems);
	}

	private static class ViewHolder {
		final ImageView ivFriendAvatar;
		final ImageView ivPersonAddedAvatar;
		final TextView tvItemText;

		ViewHolder(ImageView ivFriendAvatar, ImageView ivPersonAddedAvatar, TextView tvItemText) {
			this.ivFriendAvatar = ivFriendAvatar;
			this.ivPersonAddedAvatar = ivPersonAddedAvatar;
			this.tvItemText = tvItemText;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ActivityFeedItem item = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.activity_feed_item_layout, null);

			ImageView ivFriendAvatar = (ImageView)view.findViewById(R.id.ivFriendAvatar);
			ImageView ivPersonAddedAvatar = (ImageView)view.findViewById(R.id.ivPersonAddedAvatar);
			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			view.setTag(new ViewHolder(ivFriendAvatar, ivPersonAddedAvatar, tvItemText));

			tvItemText.setSelected(true);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (item != null && holder != null) {
			if (holder.tvItemText != null) {
				SpannableStringBuilder builder = new SpannableStringBuilder();

				String temp;
				SpannableString text;
				if (item instanceof FriendAddedEvent) {
					temp = item.getFriend().getUsername();
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = " added ";
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = ((FriendAddedEvent)item).getPersonAdded().getUsername();
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.custom_light_green)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = " as a friend";
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);
				} else if (item instanceof ClassAddedEvent) {
					temp = item.getFriend().getUsername();
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.cppgold)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = " added ";
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = ((ClassAddedEvent)item).getClassName();
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.custom_light_blue)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);

					temp = " to their schedule";
					text = new SpannableString(temp);
					text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(text);
				}

				holder.tvItemText.setText(builder);
			}

			if (holder.ivFriendAvatar != null) {
				item.getFriend().loadAvatar(holder.ivFriendAvatar);
			}

			if (holder.ivPersonAddedAvatar != null) {
				if (item instanceof FriendAddedEvent) {
					holder.ivPersonAddedAvatar.setVisibility(View.VISIBLE);
					((FriendAddedEvent)item).getPersonAdded().loadAvatar(holder.ivPersonAddedAvatar);
				}
			}
		}

		return view;
	}
}
