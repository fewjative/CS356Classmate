package edu.csupomona.cs.cs356.classmate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.ListView;
import edu.csupomona.cs.cs356.classmate.fragments.FriendsFragment;
import edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment;
import edu.csupomona.cs.cs356.classmate.fragments.TodaysFragment;

public class MainActivity extends FragmentActivity {
	private static final String STATE_FRAGMENT = "fragment";
	private static final String STATE_FRAGMENT_FRIENDS_TAB = "fragmentFriendsTab";

	private FragmentedNavigationDrawer dlDrawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setResult(RESULT_CANCELED);

		dlDrawer = (FragmentedNavigationDrawer)findViewById(R.id.dlMainDrawer);
		dlDrawer.setupDrawerConfiguration(
			(ListView)findViewById(R.id.lvDrawer),
			R.id.flContentFrame
		);

		String username = getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
		if (username == null) {
			dlDrawer.setDrawerHeader(R.string.user_menu_header);
		} else {
			dlDrawer.setDrawerHeader(username);
		}

		int name, icon;
		String[] itemName, itemIcon;

		name = getResources().getIdentifier("schedule_menu_today", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_go_to_today", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, TodaysFragment.class);

		name = getResources().getIdentifier("schedule_menu_schedule", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_go_to_today", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, TodaysFragment.class);

		dlDrawer.addHeader(R.string.user_menu_header);

		name = getResources().getIdentifier("user_menu_friends", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_person", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, FriendsFragment.class);

		name = getResources().getIdentifier("user_menu_groups", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_group", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, GroupsFragment.class);

		dlDrawer.addHeader(R.string.app_menu_header);

		name = getResources().getIdentifier("app_menu_settings", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_settings", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, GroupsFragment.class);

		name = getResources().getIdentifier("app_menu_logout", "string", this.getPackageName());
		icon = getResources().getIdentifier("ic_action_back", "drawable", this.getPackageName());
		dlDrawer.addItem(name, icon, null);

		if (savedInstanceState == null) {
			dlDrawer.selectDrawerItem(1);
		}
	}

	@Override
	protected void onPostCreate(Bundle icicle) {
		super.onPostCreate(icicle);
		dlDrawer.getDrawerToggle().syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
