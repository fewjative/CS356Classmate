package edu.csupomona.cs.cs356.classmate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.ListView;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_NAME;
import edu.csupomona.cs.cs356.classmate.drawer.FragmentNavigationDrawer;
import edu.csupomona.cs.cs356.classmate.drawer.Header;
import edu.csupomona.cs.cs356.classmate.drawer.Item;
import edu.csupomona.cs.cs356.classmate.drawer.UserHeader;

public class MainActivity extends FragmentActivity {
	private FragmentNavigationDrawer dlDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);

		setupDrawer();
	}

	private void setupDrawer() {
		dlDrawer = (FragmentNavigationDrawer)findViewById(R.id.dlDrawer);
		dlDrawer.setupDrawerConfiguration(
			  (ListView)findViewById(R.id.lvDrawer),
			  R.id.flContentFrame
		);

		String temp = getIntent().getStringExtra(INTENT_KEY_NAME);
		dlDrawer.addItem(new UserHeader(temp, R.drawable.ic_action_person));

		temp = getString(R.string.nd_schedule_today);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_go_to_today));

		temp = getString(R.string.nd_schedule_full_schedule);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_go_to_today));

		temp = getString(R.string.nd_social_header);
		dlDrawer.addItem(new Header(temp));

		temp = getString(R.string.nd_social_friends);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_person));

		temp = getString(R.string.nd_social_groups);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_group));

		temp = getString(R.string.nd_app_header);
		dlDrawer.addItem(new Header(temp));

		temp = getString(R.string.nd_app_settings);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_settings));

		temp = getString(R.string.nd_app_logout);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_back));

		dlDrawer.selectItem(dlDrawer.getFirstSelectableItem());
	}

	@Override
	protected void onPostCreate(Bundle icicle) {
		super.onPostCreate(icicle);
		dlDrawer.getToggle().syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dlDrawer.getToggle().onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (dlDrawer.getToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
