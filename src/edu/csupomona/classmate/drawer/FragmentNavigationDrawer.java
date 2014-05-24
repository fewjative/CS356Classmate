package edu.csupomona.classmate.drawer;

import com.facebook.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import edu.csupomona.classmate.R;

public class FragmentNavigationDrawer extends DrawerLayout {
	private ActionBarDrawerToggle toggle;
	private ListView lvDrawer;
	private DrawerAdapter adapter;

	private int contentPaneResId;

	private Header header;

	private Item selectedItem;
	private View selectedItemView;
	private int selectedItemPosition;
	private int lastSelectedItemPosition;

	public FragmentNavigationDrawer(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
	}

	public FragmentNavigationDrawer(Context c, AttributeSet attrs) {
		super(c, attrs);
	}

	public FragmentNavigationDrawer(Context c) {
		super(c);
	}

	public void setupDrawerConfiguration(Header header, ListView drawerListView, int contentPaneResId) {
		this.header = header;
		this.contentPaneResId = contentPaneResId;

		adapter = new DrawerAdapter(getContext());
		adapter.add(header);

		lvDrawer = drawerListView;
		lvDrawer.setAdapter(adapter);
		lvDrawer.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		lvDrawer.setOnItemClickListener(new DrawerItemListener());

		toggle = new ActionBarDrawerToggle(
			  getActivity(),
			  this,
			  R.drawable.ic_drawer,
			  R.string.global_classmate,
			  R.string.global_classmate
		) {
			@Override
			public void onDrawerClosed(View view) {
				getActivity().getActionBar().setTitle(selectedItem.title);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActivity().getActionBar().setTitle(R.string.global_classmate);
			}
		};

		setDrawerListener(toggle);
		setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);
	}

	public int getSelectedItemPos() {
		return selectedItemPosition;
	}

	public void selectItem(int position) {
		selectItem(position, true);
	}

	public void selectItem(int position, boolean closeDrawer) {
		selectItem(position, closeDrawer, null);
	}

	public void selectItem(int position, boolean closeDrawer, View v) {
		if (position < 0) {
			return;
		}

		if (selectedItem != null && selectedItemView != null && selectedItemView != v) {
			selectedItem.setChecked(false);
			View view = lvDrawer.getChildAt(selectedItemPosition);
			selectedItemView = adapter.getView(selectedItemPosition, view, this);
		}

		lastSelectedItemPosition = selectedItemPosition;
		selectedItemPosition = position;

		boolean wasNull = (selectedItem == null);
		selectedItem = (Item)adapter.getItem(selectedItemPosition);
		selectedItem.setChecked(true);

		// TODO figure out what this did
		if (v == null) {
		v = lvDrawer.getChildAt(selectedItemPosition);
		}

		v = adapter.getView(selectedItemPosition, v, this);

		selectedItemView = v;

		if (selectedItem.title.contentEquals(getResources().getString(R.string.nd_app_logout))) {
			attemptLogout();
			return;
		}

		Fragment fragment = null;
		try {
			fragment = selectedItem.fragment.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} finally {
			if (fragment != null) {
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(contentPaneResId, fragment).commit();
				if (closeDrawer) {
					closeDrawer(lvDrawer);
				}

				if (wasNull) {
					getActivity().getActionBar().setTitle(selectedItem.title);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (isDrawerOpen()) {
					closeDrawer(lvDrawer);
				} else {
					openDrawer(lvDrawer);
				}

				return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void addItem(DrawerListItem item) {
		adapter.add(item);
	}

	private FragmentActivity getActivity() {
		return (FragmentActivity)getContext();
	}

	public ActionBarDrawerToggle getToggle() {
		return toggle;
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(lvDrawer);
	}

	public int getFirstSelectableItem() {
		for (int i = 0; i < lvDrawer.getCount(); i++) {
			if (adapter.isEnabled(i)) {
				return i;
			}
		}

		return -1;
	}

	private class DrawerItemListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO See if passing v will be more efficient
			selectItem(position, true, v);
		}
	}

	private void attemptLogout() {
		AlertDialog.Builder logoutDialog = new AlertDialog.Builder(getActivity());
		logoutDialog.setTitle(R.string.dialog_logout_title);
		logoutDialog.setMessage(R.string.dialog_logout);
		logoutDialog.setPositiveButton(R.string.global_action_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//logout of facebook session if there is one
				Session session = Session.getActiveSession();
				if (session != null) {
					session.closeAndClearTokenInformation();
				}
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
			}
		});

		logoutDialog.setNegativeButton(R.string.global_action_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				selectItem(lastSelectedItemPosition, false);
			}
		});

		AlertDialog dialog = logoutDialog.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
