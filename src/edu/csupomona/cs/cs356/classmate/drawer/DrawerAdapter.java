package edu.csupomona.cs.cs356.classmate.drawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

class DrawerAdapter extends ArrayAdapter<DrawerListItem> {
	DrawerAdapter(Context c) {
		super(c, 0);
	}

	@Override
	public void add(DrawerListItem drawerItem) {
		super.add(drawerItem);
		if (drawerItem instanceof Item) {
			super.add(new ItemDivider());
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return (getItem(position) instanceof Item) ? 1 : 0;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItem(position) instanceof Item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position).getView(getContext(), convertView);
	}
}
