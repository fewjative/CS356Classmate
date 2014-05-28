package edu.csupomona.classmate.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.TimeSlot;

import java.util.List;

public class FreeTimeTabScheduleAdapter extends ArrayAdapter<TimeSlot> {
	private boolean outside_source = false;
	
	public FreeTimeTabScheduleAdapter(Context context, List<TimeSlot> freetime) {
		super(context, 0, freetime);
	}
	


	private static class ViewHolder {
		
		final TextView tvFreeTime;
	

		ViewHolder(TextView tvFreeTime) {
		
			this.tvFreeTime = tvFreeTime;
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TimeSlot ts = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		Button btnRemoveClass = null;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.freetime_schedule_list_item,null);

			TextView tvFreeTime = (TextView)view.findViewById(R.id.tvFreeTime);
			
			view.setTag(new ViewHolder(tvFreeTime));

		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (ts != null && holder != null) {
			if (holder.tvFreeTime != null) {
				holder.tvFreeTime.setText("Time: " + ts.getFullTime());
			}

		}
		
		return view;
	}

}