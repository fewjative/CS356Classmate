package edu.csupomona.classmate.fragments;

import java.util.List;

import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Activity;
import edu.csupomona.classmate.abstractions.User;
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

public class ActivityFeedAdapter extends ArrayAdapter<Activity>{
	
	
	public ActivityFeedAdapter(Context context, List<Activity> activities){
		super(context, 0, activities);
	}
	
	private static class ViewHolder {
		final ImageView ivAvatarFriend;
		final ImageView ivAvatarAdded;
		final TextView tvItemName;

		ViewHolder(ImageView ivAvatarFriend, ImageView ivAvatarAdded, TextView tvItemName){
			this.ivAvatarFriend = ivAvatarFriend;
			this.ivAvatarAdded = ivAvatarAdded;
			this.tvItemName = tvItemName;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Activity a = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		
		if (view == null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.activity_feed_item_layout, null);
			
			ImageView ivAvatarFriend = (ImageView)view.findViewById(R.id.ivAvatarFriend);
			ImageView ivAvatarAdded = (ImageView)view.findViewById(R.id.ivAvatarAdded);
			TextView tvItemName = (TextView)view.findViewById(R.id.tvItemName);
			view.setTag(new ViewHolder(ivAvatarFriend, ivAvatarAdded, tvItemName));
		}
		
		Object tag = view.getTag();
		if (tag instanceof ViewHolder){
			holder = (ViewHolder)tag;
		}
		
		if (a != null && holder != null){
			User friend = a.getFriend();
			User added = a.getAdded();
			
			if (holder.tvItemName != null){
				SpannableStringBuilder builder = new SpannableStringBuilder();
				
				SpannableString username = new SpannableString(friend.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);
				
				
				
				builder.append(" added ");
				if(added.getID()!=0) {
					SpannableString friendName = new SpannableString(added.getUsername());
					friendName.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, friendName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(friendName);
					
					
				}else{
					SpannableString className = new SpannableString(a.getClassName());
					className.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, className.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(className);
					
					
				}
				
				holder.tvItemName.setText(builder);
			}
			if (holder.ivAvatarFriend != null) {
				holder.ivAvatarFriend.setVisibility(View.VISIBLE);
				friend.loadAvatar(holder.ivAvatarFriend);
			}
			if (holder.ivAvatarAdded != null) {
				holder.ivAvatarAdded.setVisibility(View.VISIBLE);
				added.loadAvatar(holder.ivAvatarAdded);
			}
		}
		return view;
	}
}
