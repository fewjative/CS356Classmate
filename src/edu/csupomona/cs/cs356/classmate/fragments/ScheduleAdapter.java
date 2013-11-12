package edu.csupomona.cs.cs356.classmate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Section> implements View.OnClickListener {
	public ScheduleAdapter(Context context, List<Section> schedule) {
		super(context, 0, schedule);
	}

	private static class ViewHolder {
		final TextView tvItemText;
		final ImageButton btnRemoveClass;

		ViewHolder(TextView tvItemText, ImageButton btnRemoveClass) {
			this.tvItemText = tvItemText;
			this.btnRemoveClass = btnRemoveClass;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Section s = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, null);

			TextView tvItemText = (TextView)view.findViewById(R.id.tvItemText);
			ImageButton btnRemoveClass = (ImageButton)view.findViewById(R.id.btnRemoveClass);
			view.setTag(new ViewHolder(tvItemText, btnRemoveClass));

			tvItemText.setSelected(true);

			btnRemoveClass.setTag(s);
			btnRemoveClass.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (s != null && holder != null) {
			if (holder.tvItemText != null) {
				holder.tvItemText.setText(String.format("%s %s Section %s", s.major_short, s.class_num, s.section));
			}
		}

		return view;
	}

	public void onClick(View v) {
		Section s = (Section)v.getTag();
		switch (v.getId()) {
			case R.id.btnRemoveClass:
				removeClass(s);
				break;
		}
	}

	public void removeClass(final Section s) {
		//...
	}
}