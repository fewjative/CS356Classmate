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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	final String[] fragments = {
		"edu.csupomona.cs.cs356.classmate.fragments.FriendsFragment",
		"edu.csupomona.cs.cs356.classmate.fragments.AnotherFragment"
	};

	private int activeFragmentID;

	private String[] optionList;
	private DrawerLayout dlMainDrawer;
	private ListView lvDrawer;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main_activity);

		optionList = getResources().getStringArray(R.array.drawerItemList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item_layout, optionList);

		dlMainDrawer = (DrawerLayout)findViewById(R.id.dlMainDrawer);
		lvDrawer = (ListView)findViewById(R.id.lvDrawer);
		
		//final TextView tvUser = (TextView)getLayoutInflater().inflate(R.layout.drawer_list_item_layout, null);
		//tvUser.setText(getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME));
		//lvDrawer.addHeaderView(tvUser, null, false);
		
		final TextView tvLogout = (TextView)getLayoutInflater().inflate(R.layout.drawer_list_item_layout, null);
		tvLogout.setText("Logout");
		lvDrawer.addFooterView(tvLogout);
		
		lvDrawer.setAdapter(adapter);
		
		lvDrawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				if (id == tvLogout.getId()) {
					attemptLogout();
					return;
				}
				
				activeFragmentID = pos;
				dlMainDrawer.closeDrawer(lvDrawer);
			}
		});

		activeFragmentID = 0;
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.flContentFrame, Fragment.instantiate(MainActivity.this, fragments[0]));
		tx.commit();
		
		getActionBar().setTitle(optionList[activeFragmentID]);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawerToggle = new CustomActionBarDrawerToggle(this, dlMainDrawer);
		dlMainDrawer.setDrawerListener(drawerToggle);
	}

	private void attemptLogout() {
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setTitle(R.string.logoutConfirmationTitle);
		d.setMessage(getResources().getString(R.string.logoutConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
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
				break;
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
			FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.flContentFrame, Fragment.instantiate(MainActivity.this, fragments[activeFragmentID]));
			tx.commit();

			getActionBar().setTitle(optionList[activeFragmentID]);
			invalidateOptionsMenu();
		}
	}
}
