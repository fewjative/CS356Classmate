package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.csupomona.cs.cs356.classmate.fragments.FriendsFragment;
import edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment;
import edu.csupomona.cs.cs356.classmate.utils.MenuAdapter;
import edu.csupomona.cs.cs356.classmate.utils.MenuItemModel;

public class MainActivity extends FragmentActivity {
	private static final String STATE_FRAGMENT = "fragment";
	private static final String STATE_FRAGMENT_FRIENDS_TAB = "fragmentFriendsTab";

	private ListView lvDrawer;
	private DrawerLayout dlMainDrawer;
	private MenuAdapter adapter;
	private ActionBarDrawerToggle drawerToggle;
	
	private MenuItemModel activeDrawerItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setResult(RESULT_CANCELED);

		adapter = buildDrawer();
		dlMainDrawer = (DrawerLayout)findViewById(R.id.dlMainDrawer);
		lvDrawer = (ListView)findViewById(R.id.lvDrawer);
		lvDrawer.setAdapter(adapter);
		lvDrawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				MenuItemModel selecedDrawerItem = adapter.getItem(pos);
				if (activeDrawerItem != null && activeDrawerItem.getTitleRes() == selecedDrawerItem.getTitleRes()) {
					dlMainDrawer.closeDrawer(lvDrawer);
					return;
				} else if (selecedDrawerItem.getTitleRes() == R.string.app_menu_logout) {
					attemptLogout();
					return;
				}

				activeDrawerItem = selecedDrawerItem;
				dlMainDrawer.closeDrawer(lvDrawer);
			}
		});

		if (savedInstanceState == null) {
			activeDrawerItem = adapter.getItem(1);
			swapFragments();
		} else {
			// Technically I should set the tab here, but I don't think it's needed
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		drawerToggle = new CustomActionBarDrawerToggle(this, dlMainDrawer);
		dlMainDrawer.setDrawerListener(drawerToggle);
	}

	private MenuAdapter buildDrawer() {
		MenuAdapter mAdapter = new MenuAdapter(this);

		String userName = getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
		if (userName == null) {
			mAdapter.addMasterHeader(R.string.user_menu_header);
		} else {
			mAdapter.addMasterHeader(userName);
		}

		int name, icon;
		String[] itemName, itemIcon;

		itemName = getResources().getStringArray(R.array.user_menu_items);
		itemIcon = getResources().getStringArray(R.array.user_menu_items_icon);
		assert itemName.length == itemIcon.length;
		for (int i = 0; i < itemName.length; i++) {
			name = getResources().getIdentifier(itemName[i], "string", this.getPackageName());
			icon = getResources().getIdentifier(itemIcon[i], "drawable", this.getPackageName());
			MenuItemModel mItem = new MenuItemModel(name, icon);
			mAdapter.addItem(mItem);
		}

		mAdapter.addHeader(R.string.schedule_menu_header);

		itemName = getResources().getStringArray(R.array.schedule_menu_items);
		itemIcon = getResources().getStringArray(R.array.schedule_menu_items_icon);
		assert itemName.length == itemIcon.length;
		for (int i = 0; i < itemName.length; i++) {
			name = getResources().getIdentifier(itemName[i], "string", this.getPackageName());
			icon = getResources().getIdentifier(itemIcon[i], "drawable", this.getPackageName());
			MenuItemModel mItem = new MenuItemModel(name, icon);
			mAdapter.addItem(mItem);
		}

		mAdapter.addHeader(R.string.app_menu_header);

		itemName = getResources().getStringArray(R.array.app_menu_items);
		itemIcon = getResources().getStringArray(R.array.app_menu_items_icon);
		assert itemName.length == itemIcon.length;
		for (int i = 0; i < itemName.length; i++) {
			name = getResources().getIdentifier(itemName[i], "string", this.getPackageName());
			icon = getResources().getIdentifier(itemIcon[i], "drawable", this.getPackageName());
			MenuItemModel mItem = new MenuItemModel(name, icon);
			mAdapter.addItem(mItem);
		}

		return mAdapter;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (activeDrawerItem == null) {
			outState.putInt(STATE_FRAGMENT, R.string.user_menu_friends);
		} else {
			outState.putInt(STATE_FRAGMENT, activeDrawerItem.getTitleRes());
		}
	}
	
	private void attemptLogout() {
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setTitle(R.string.logoutConfirmationTitle);
		d.setMessage(getResources().getString(R.string.logoutConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setCanceledOnTouchOutside(true);
		d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_OK);
				finish();
			}
		});

		d.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
			}
		});

		d.show();
	}

	@Override
	protected void onPostCreate(Bundle icicle) {
		super.onPostCreate(icicle);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = dlMainDrawer.isDrawerOpen(lvDrawer);
		menu.findItem(R.id.action_save).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (dlMainDrawer.isDrawerOpen(lvDrawer)) {
					dlMainDrawer.closeDrawer(lvDrawer);
				} else {
					dlMainDrawer.openDrawer(lvDrawer);
				}

				return true;
			/*case KeyEvent.KEYCODE_BACK:
				//if (dlMainDrawer.isDrawerOpen(lvDrawer)) {
				//	break;
				//}

				attemptLogout();
				return true;*/
		}

		return super.onKeyDown(keyCode, event);
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
		CustomActionBarDrawerToggle(Activity a, DrawerLayout drawerLayout){
			super(
				a,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.drawerOpen,
				R.string.drawerClose
			);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(R.string.ClassmateAppName);
			invalidateOptionsMenu();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			swapFragments();
		}
	}

	private void swapFragments() {
		String conicalPath = null;
		switch (activeDrawerItem.getTitleRes()) {
			case R.string.user_menu_friends:
				conicalPath = FriendsFragment.class.getCanonicalName();
				break;
			case R.string.user_menu_groups:
				conicalPath = GroupsFragment.class.getCanonicalName();
				break;
			case R.string.schedule_menu_today:
				conicalPath = GroupsFragment.class.getCanonicalName();
				break;
			case R.string.schedule_menu_schedule:
				conicalPath = GroupsFragment.class.getCanonicalName();
				break;
			case R.string.app_menu_settings:
				conicalPath = GroupsFragment.class.getCanonicalName();
				break;
		}

		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.flContentFrame, Fragment.instantiate(MainActivity.this, conicalPath));
		tx.commit();

		getActionBar().setTitle(activeDrawerItem.getTitleRes());
		invalidateOptionsMenu();
	}
}
