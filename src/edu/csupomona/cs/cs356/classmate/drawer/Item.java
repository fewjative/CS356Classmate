package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;

public class Item extends DrawerListItem {
	static int numItems;

	Class<? extends Fragment> fragment;

	int iconResId;
	int count;

	private boolean checked;

	public Item(Class<? extends Fragment> fragment, String title) {
		this(fragment, title, 0);
	}

	public Item(Class<? extends Fragment> fragment, String title, int iconResId) {
		this(fragment, title, iconResId, 0);
	}

	public Item(Class<? extends Fragment> fragment, String title, int iconResId, int count) {
		super(title);
		this.fragment = fragment;
		this.iconResId = iconResId;
		this.count = count;
		this.checked = false;
		numItems++;
	}

	@Override
	View getView(Context c, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v != null) {
			TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
			if (checked) {
				v.setBackgroundResource(R.color.cppgold_trans_darker);
				tvTitle.setTextColor(c.getResources().getColor(R.color.black_trans));
				tvTitle.setTypeface(null, Typeface.BOLD);
			} else {
				v.setBackgroundResource(android.R.color.transparent);
				tvTitle.setTextColor(c.getResources().getColor(R.color.white_trans));
				tvTitle.setTypeface(null, Typeface.NORMAL);
			}

			return v;
		}

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_item_layout, parent, false);
		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setText(title);

		ImageView ivIcon = (ImageView)v.findViewById(R.id.ivIcon);
		if (0 < iconResId) {
			ivIcon.setVisibility(View.VISIBLE);
			ivIcon.setImageResource(iconResId);
		} else {
			ivIcon.setVisibility(View.GONE);
		}

		TextView tvCounter = (TextView)v.findViewById(R.id.tvCounter);
		if (0 < count) {
			tvCounter.setVisibility(View.VISIBLE);
			tvCounter.setText(Integer.toString(count));
		} else {
			tvCounter.setVisibility(View.GONE);
		}

		if (checked) {
			v.setBackgroundResource(R.color.cppgold_trans_darker);
			tvTitle.setTextColor(c.getResources().getColor(R.color.black_trans));
			tvTitle.setTypeface(null, Typeface.BOLD);
		} else {
			v.setBackgroundResource(android.R.color.transparent);
			tvTitle.setTextColor(c.getResources().getColor(R.color.white_trans));
			tvTitle.setTypeface(null, Typeface.NORMAL);
		}

		return v;
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public void toggle() {
		checked = !checked;
	}
}
