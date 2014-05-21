package edu.csupomona.classmate.fragments;

import java.util.List;

import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActivityFeedAdapter extends ArrayAdapter<Activity>{
	
	
	public ActivityFeedAdapter(Context context, List<Activity> activities){
		super(context, 0, activities);
		
	}
	
	private static class ViewHolder {
		final TextView textUserName;
		final TextView added;
		final TextView textFriendUserName;
		final TextView textClassName;
		
		ViewHolder(TextView textUserName, TextView added, TextView textFriendUserName, TextView textClassName){
			this.textUserName = textUserName;
			this.added = added;
			this.textFriendUserName = textFriendUserName;
			this.textClassName = textClassName;
			
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Activity a = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		
		if (view == null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.activity_feed_item_layout, null);
			
			TextView textUserName = (TextView)view.findViewById(R.id.tvItemTextUsername);
			TextView textFriendUserName = (TextView)view.findViewById(R.id.tvItemTextFriendUsername);
			TextView textClassName = (TextView)view.findViewById(R.id.tvItemTextClassname);
			TextView added = (TextView)view.findViewById(R.id.added);
			view.setTag(new ViewHolder(textUserName, added, textFriendUserName, textClassName));
		}
		
		Object tag = view.getTag();
		if (tag instanceof ViewHolder){
			holder = (ViewHolder)tag;
		}
		if (a != null && holder != null){
			if (holder.textUserName != null){
				SpannableStringBuilder builder = new SpannableStringBuilder();
				
				SpannableString username = new SpannableString(a.getUsername());
				username.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				builder.append(username);
				
				builder.append(" added ");
				if(!a.getFriendUsername().equals("0")){
					SpannableString friendName = new SpannableString(a.getFriendUsername());
					friendName.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, friendName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(friendName);
				}else{
					SpannableString className = new SpannableString(a.getClassName());
					className.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, className.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					builder.append(className);
				}
				
				builder.append(".");
				holder.textUserName.setText(builder);
			}
		}
		return view;
	}
}
