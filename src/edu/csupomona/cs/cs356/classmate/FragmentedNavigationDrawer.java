package edu.csupomona.cs.cs356.classmate;

import android.app.ActionBar;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class FragmentedNavigationDrawer extends DrawerLayout {
	private ActionBarDrawerToggle drawerToggle;

	private ListView lvDrawer;
	private NavigationDrawerAdapter drawerAdapter;
	private ArrayList<FragmentNavigationItem> drawerItemList;
	private int drawerContainerRes;

	private int selectedItemPosition;

	private static CharSequence lastTitle;

	private View selectedItem;

	public FragmentedNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FragmentedNavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FragmentedNavigationDrawer(Context context) {
		super(context);
	}

	public void setupDrawerConfiguration(ListView drawerListView, int drawerContainerRes) {
		drawerItemList = new ArrayList<FragmentNavigationItem>();
		drawerAdapter = new NavigationDrawerAdapter(getActivity());

		this.drawerContainerRes = drawerContainerRes;

		lvDrawer = drawerListView;
		lvDrawer.setAdapter(drawerAdapter);
		lvDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());

		drawerToggle = setupDrawerToggle();
		setDrawerListener(drawerToggle);
		setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
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

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(
			getActivity(),
			this,
			R.drawable.ic_drawer,
			R.string.drawerOpen,
			R.string.drawerClose
		) {
			@Override
			public void onDrawerClosed(View view) {
				setTitle(lastTitle);
				//getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				setTitle(R.string.ClassmateAppName);
				((MainActivity)getActivity()).updateFriendRequestsNum();
				//getActivity().invalidateOptionsMenu();
			}
		};
	}

	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}

	public void selectDrawerItem(int position) {
		FragmentNavigationItem navItem = drawerItemList.get(position);
		if (navItem.titleRes == R.string.app_menu_logout) {
			attemptLogout();
			return;
		}

		Fragment fragment = null;
		try {
			fragment = navItem.fragmentClass.newInstance();
			Bundle args = navItem.fragmentArgs;
			if (args != null) {
				fragment.setArguments(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(drawerContainerRes);
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(drawerContainerRes, fragment).commit();

		lvDrawer.setItemChecked(position, true);
		if (navItem.title == null) {
			setTitle(navItem.titleRes);
		} else {
			setTitle(navItem.title);
		}

		lastTitle = getActionBar().getTitle();
		closeDrawer(lvDrawer);
		selectedItemPosition = position;
	}

	public void setDrawerHeader(int title) {
		drawerAdapter.add(new NavigationDrawerItemModel(title, -1, true, true));
		drawerItemList.add(null);
	}

	public void setDrawerHeader(CharSequence title) {
		drawerAdapter.add(new NavigationDrawerItemModel(title, -1, true, true));
		drawerItemList.add(null);
	}

	public void addHeader(int title) {
		drawerAdapter.add(new NavigationDrawerItemModel(title, -1, true, false));
		drawerItemList.add(null);
	}

	public void addHeader(CharSequence title) {
		drawerAdapter.add(new NavigationDrawerItemModel(title, -1, true, false));
		drawerItemList.add(null);
	}

	public NavigationDrawerItemModel addItem(int title, int icon, Class<? extends Fragment> fragmentClass) {
		NavigationDrawerItemModel item = new NavigationDrawerItemModel(title, icon, false, false);
		drawerAdapter.add(item);
		drawerItemList.add(new FragmentNavigationItem(title, fragmentClass));
		return item;
	}

	public NavigationDrawerItemModel addItem(CharSequence title, int icon, Class<? extends Fragment> fragmentClass) {
		NavigationDrawerItemModel item = new NavigationDrawerItemModel(title, icon, false, false);
		drawerAdapter.add(item);
		drawerItemList.add(new FragmentNavigationItem(title, fragmentClass));
		return item;
	}

	private FragmentActivity getActivity() {
		return (FragmentActivity)getContext();
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	public NavigationDrawerAdapter getAdapter() {
		return drawerAdapter;
	}

	public ListView getListView() {
		return lvDrawer;
	}

	public ActionBarDrawerToggle getDrawerToggle() {
		return drawerToggle;
	}

	private void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	private void setTitle(int title) {
		getActionBar().setTitle(title);
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(lvDrawer);
	}

	private class FragmentNavigationItem {
		private int titleRes;
		private CharSequence title;
		private Class<? extends Fragment> fragmentClass;
		private Bundle fragmentArgs;

		public FragmentNavigationItem(int title, Class<? extends Fragment> fragmentClass) {
			this(title, fragmentClass, null);
		}

		public FragmentNavigationItem(CharSequence title, Class<? extends Fragment> fragmentClass) {
			this(title, fragmentClass, null);
		}

		public FragmentNavigationItem(int title, Class<? extends Fragment> fragmentClass, Bundle args) {
			this.titleRes = title;
			this.fragmentClass = fragmentClass;
			this.fragmentArgs = args;
		}

		public FragmentNavigationItem(CharSequence title, Class<? extends Fragment> fragmentClass, Bundle args) {
			this.title = title;
			this.fragmentClass = fragmentClass;
			this.fragmentArgs = args;
		}
	}

	private class FragmentDrawerItemListener implements ListView.OnItemClickListener {


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (selectedItem == null) {
				selectedItem = drawerAdapter.firstListItem;
			}

			selectedItem.findViewById(R.id.menuitem_content).setBackgroundResource(0);

			selectedItem = view;
			selectedItem.findViewById(R.id.menuitem_content).setBackgroundResource(R.color.cppgold_trans_darker);

			selectDrawerItem(position);
		}
	}

	private void attemptLogout() {
		AlertDialog d = new AlertDialog.Builder(getActivity()).create();
		d.setTitle(R.string.logoutConfirmationTitle);
		d.setMessage(getResources().getString(R.string.logoutConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setCanceledOnTouchOutside(true);
		d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				getActivity().setResult(RESULT_OK);
				getActivity().finish();
			}
		});

		d.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				getActivity().setResult(RESULT_CANCELED);
			}
		});

		d.show();
	}
}
