package edu.csupomona.classmate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.CalendarEvent;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarFeedAdapter extends ArrayAdapter<CalendarEvent> {
	public CalendarFeedAdapter(Context context, List<CalendarEvent> calendarEvents) {
		super(context, 0, calendarEvents);
	}

	private static class ViewHolder {
		final ImageView ivImage;
		final TextView tvTitle;
		final TextView tvDesc;
		final TextView tvDate;
		final TextView tvDay;
		final LinearLayout llProgressBar;

		ViewHolder(ImageView ivImage, TextView tvTitle, TextView tvDesc, TextView tvDate, TextView tvDay, LinearLayout llProgressBar) {
			this.ivImage = ivImage;
			this.tvTitle = tvTitle;
			this.tvDesc = tvDesc;
			this.tvDate = tvDate;
			this.tvDay = tvDay;
			this.llProgressBar = llProgressBar;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarEvent event = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.calendar_event_item_layout, null);

			ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
			TextView tvDesc = (TextView)view.findViewById(R.id.tvDesc);
			TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
			TextView tvDay = (TextView)view.findViewById(R.id.tvDay);
			LinearLayout llProgressBar = (LinearLayout)view.findViewById(R.id.llProgressBar);
			view.setTag(new ViewHolder(ivImage, tvTitle, tvDesc, tvDate, tvDay, llProgressBar));
		//}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (event != null && holder != null) {
			if (holder.ivImage != null) {
				event.loadImage(holder.ivImage, holder.llProgressBar);
			}

			if (holder.tvTitle != null) {
				holder.tvTitle.setText(event.getTitle());
			}

			if (holder.tvDesc != null) {
				holder.tvDesc.setText(event.getDesc());
			}

			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = df.parse(event.getDate());
				if (holder.tvDate != null) {
					holder.tvDate.setText(DateFormatSymbols.getInstance().getMonths()[date.getMonth()]);
				}

				if (holder.tvDay != null) {
					holder.tvDay.setText(String.format("%d", date.getDate()));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return view;
	}
}
