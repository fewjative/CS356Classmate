package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItemModel> {
	static View firstListItem;
	private Intent intent;

	public NavigationDrawerAdapter(Context context) {
		super(context, 0);
		this.intent = ((Activity)context).getIntent();
	}

	public void addMasterHeader(int title) {
		add(new NavigationDrawerItemModel(title, -1, true, true));
	}

	public void addMasterHeader(CharSequence title) {
		add(new NavigationDrawerItemModel(title, -1, true, true));
	}

	public void addHeader(int title) {
		add(new NavigationDrawerItemModel(title, -1, true, false));
	}

	public void addHeader(CharSequence title) {
		add(new NavigationDrawerItemModel(title, -1, true, false));
	}

	public void addItem(int title, int icon) {
		add(new NavigationDrawerItemModel(title, icon, false, false));
	}

	public void addItem(CharSequence title, int icon) {
		add(new NavigationDrawerItemModel(title, icon, false, false));
	}

	public void addItem(NavigationDrawerItemModel itemModel) {
		add(itemModel);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).isHeader() ? 0 : 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader();
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
		NavigationDrawerItemModel item = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		View avatarView;

		if (view == null) {
			int layout;
			if (item.isHeader()) {
				if (item.isMasterHeader()) {
					layout = R.layout.menu_master_header;
				} else {
					layout = R.layout.menu_header;
				}
			} else {
				layout = R.layout.menu_item;
			}

			view = LayoutInflater.from(getContext()).inflate(layout, null);
			avatarView = view;

			TextView text1 = (TextView)view.findViewById(R.id.menurow_title);
			ImageView image1 = (ImageView)view.findViewById(R.id.menurow_icon);

			if (layout == R.layout.menu_master_header) {
				ImageView avatar = (ImageView)view.findViewById(R.id.ivAvatar);

				//int id = intent.getIntExtra(LoginActivity.INTENT_KEY_USERID, 0);
				avatar.setImageResource(R.drawable.ic_action_person);

			}

			TextView textcounter1 = (TextView)view.findViewById(R.id.menurow_counter);
			view.setTag(new ViewHolder(text1, image1, textcounter1));

			if (firstListItem == null && !item.isHeader() && !item.isMasterHeader()) {
				firstListItem = view;
				firstListItem.findViewById(R.id.menuitem_content).setBackgroundResource(R.color.cppgold_trans_darker);
			}
		}

		if (holder == null) {
			Object tag = view.getTag();
			if (tag instanceof ViewHolder) {
				holder = (ViewHolder)tag;
			}
		}

		if (item != null && holder != null) {
			if (holder.textHolder != null) {
				if (item.getTitle() == null) {
					holder.textHolder.setText(item.getTitleRes());
				} else {
					holder.textHolder.setText(item.getTitle());
				}
			}

			if (holder.textCounterHolder != null) {
				if (item.getCounter() > 0) {
					holder.textCounterHolder.setVisibility(View.VISIBLE);
					holder.textCounterHolder.setText(Integer.toString(item.getCounter()));
				} else {
					holder.textCounterHolder.setVisibility(View.GONE);
				}
			}

			if (holder.imageHolder != null) {
				if (item.getIconRes() > 0) {
					holder.imageHolder.setVisibility(View.VISIBLE);
					holder.imageHolder.setImageResource(item.getIconRes());
				} else {
					holder.imageHolder.setVisibility(View.GONE);
				}
			}
		}

		return view;
	}
}
