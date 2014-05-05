package edu.csupomona.classmate.drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.classmate.R;

public class Item extends DrawerListItem {
	static int numItems;

	Class<? extends Fragment> fragment;

	int iconSelectedResId;
	int iconResId;
	int count;

	private boolean checked;

	public Item(Class<? extends Fragment> fragment, String title) {
		this(fragment, title, 0, 0);
	}

	public Item(Class<? extends Fragment> fragment, String title, int iconResId) {
		this(fragment, title, iconResId, iconResId, 0);
	}

	public Item(Class<? extends Fragment> fragment, String title, int iconResId, int iconSelectedResId) {
		this(fragment, title, iconResId, iconSelectedResId, 0);
	}

	public Item(Class<? extends Fragment> fragment, String title, int iconResId, int iconSelectedResId, int count) {
		super(title);
		this.fragment = fragment;
		this.iconResId = iconResId;
		this.iconSelectedResId = iconSelectedResId;
		this.count = count;
		this.checked = false;
		numItems++;
	}

	@Override
	View getView(Context c, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v != null) {
			TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
			ImageView ivIcon = (ImageView)v.findViewById(R.id.ivIcon);
			if (tvTitle != null && ivIcon != null) {
				tvTitle.setSelected(checked);
				if (checked) {
					v.setBackgroundResource(R.color.cppgold_trans_darker);
					tvTitle.setTypeface(null, Typeface.BOLD);
					ivIcon.setImageResource(iconSelectedResId);
				} else {
					v.setBackgroundResource(android.R.color.transparent);
					tvTitle.setTypeface(null, Typeface.NORMAL);
					ivIcon.setImageResource(iconResId);
				}

				return v;
			} else {
				System.out.println("classm " + convertView.getClass().getName());
			}
		}

		v = LayoutInflater.from(c).inflate(R.layout.drawer_item_item_layout, parent, false);

		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
		tvTitle.setSelected(checked);
		tvTitle.setText(title);

		ImageView ivIcon = (ImageView)v.findViewById(R.id.ivIcon);
		ivIcon.setVisibility(0 < iconResId ? View.VISIBLE : View.GONE);

		TextView tvCounter = (TextView)v.findViewById(R.id.tvCounter);
		if (0 < count) {
			tvCounter.setVisibility(View.VISIBLE);
			tvCounter.setText(Integer.toString(count));
		} else {
			tvCounter.setVisibility(View.GONE);
		}

		if (checked) {
			v.setBackgroundResource(R.color.cppgold_trans_darker);
			tvTitle.setTypeface(null, Typeface.BOLD);
			ivIcon.setImageResource(iconSelectedResId);
		} else {
			v.setBackgroundResource(android.R.color.transparent);
			tvTitle.setTypeface(null, Typeface.NORMAL);
			ivIcon.setImageResource(iconResId);
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
