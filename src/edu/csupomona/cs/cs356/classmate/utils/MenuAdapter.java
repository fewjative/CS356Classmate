package edu.csupomona.cs.cs356.classmate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.csupomona.cs.cs356.classmate.R;

public class MenuAdapter extends ArrayAdapter<MenuItemModel> {
	public MenuAdapter(Context context) {
		super(context, 0);
	}

	public void addHeader(int title) {
		add(new MenuItemModel(title, -1, true));
	}

	public void addHeader(CharSequence title) {
		add(new MenuItemModel(title, -1, true));
	}

	public void addItem(int title, int icon) {
		add(new MenuItemModel(title, icon, false));
	}

	public void addItem(CharSequence title, int icon) {
		add(new MenuItemModel(title, icon, false));
	}

	public void addItem(MenuItemModel itemModel) {
		add(itemModel);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).isHeader ? 0 : 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader;
	}

	private static class ViewHolder {
		final TextView textHolder;
		final ImageView imageHolder;
		final TextView textCounterHolder;

		ViewHolder(TextView text1, ImageView image1, TextView textcounter1) {
			this.textHolder = text1;
			this.imageHolder = image1;
			this.textCounterHolder = textcounter1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuItemModel item = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			int layout = item.isHeader ? R.layout.menu_row_header : R.layout.menu_row_counter;

			view = LayoutInflater.from(getContext()).inflate(layout, null);

			TextView text1 = (TextView)view.findViewById(R.id.menurow_title);
			ImageView image1 = (ImageView)view.findViewById(R.id.menurow_icon);
			TextView textcounter1 = (TextView)view.findViewById(R.id.menurow_counter);
			view.setTag(new ViewHolder(text1, image1, textcounter1));
		}

		if (holder == null && view != null) {
			Object tag = view.getTag();
			if (tag instanceof ViewHolder) {
				holder = (ViewHolder)tag;
			}
		}

		if (item != null && holder != null) {
			if (holder.textHolder != null) {
				if (item.titleString == null) {
					holder.textHolder.setText(item.title);
				} else {
					holder.textHolder.setText(item.titleString);
				}
			}

			if (holder.textCounterHolder != null) {
				if (item.counter > 0) {
					holder.textCounterHolder.setVisibility(View.VISIBLE);
					holder.textCounterHolder.setText(Integer.toString(item.counter));
				} else {
					holder.textCounterHolder.setVisibility(View.GONE);
				}
			}

			if (holder.imageHolder != null) {
				if (item.icon > 0) {
					holder.imageHolder.setVisibility(View.VISIBLE);
					holder.imageHolder.setImageResource(item.icon);
				} else {
					holder.imageHolder.setVisibility(View.GONE);
				}
			}
		}

		return view;
	}
}
