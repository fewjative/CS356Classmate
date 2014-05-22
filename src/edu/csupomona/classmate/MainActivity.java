package edu.csupomona.classmate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.ListView;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.drawer.FragmentNavigationDrawer;
import edu.csupomona.classmate.drawer.Header;
import edu.csupomona.classmate.drawer.Item;
import edu.csupomona.classmate.drawer.UserHeader;
import edu.csupomona.classmate.fragments.AddClassEventFragment;
import edu.csupomona.classmate.fragments.BooksFragment;
import edu.csupomona.classmate.fragments.FriendsFragment;
import edu.csupomona.classmate.fragments.GroupsFragment;
import edu.csupomona.classmate.fragments.ScheduleFragment;
import edu.csupomona.classmate.fragments.SettingsFragment;
import edu.csupomona.classmate.fragments.UnderConstructionFragment;

public class MainActivity extends FragmentActivity {
	private FragmentNavigationDrawer dlDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);

		setupDrawer();
		if (savedInstanceState == null) {
			dlDrawer.selectItem(dlDrawer.getFirstSelectableItem());
		} else {
			dlDrawer.selectItem(savedInstanceState.getInt(Constants.INTENT_KEY_SELECTEDITEMPOS, dlDrawer.getFirstSelectableItem()));
		}
	}

	private void setupDrawer() {
		dlDrawer = (FragmentNavigationDrawer)findViewById(R.id.dlDrawer);
		dlDrawer.setupDrawerConfiguration(
			new UserHeader((User)getIntent().getParcelableExtra(INTENT_KEY_USER)),
			(ListView)findViewById(R.id.lvDrawer),
			R.id.flContentFrame
		);

		String temp = getString(R.string.nd_feed_activity);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_share));

		temp = getString(R.string.nd_feed_events);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_event));

		temp = getString(R.string.nd_feed_news);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_chat));

		temp = getString(R.string.nd_schedule_header);
		dlDrawer.addItem(new Header(temp));

		temp = getString(R.string.nd_schedule_view);
		dlDrawer.addItem(new Item(ScheduleFragment.class, temp, R.drawable.ic_action_go_to_schedule));

		temp = getString(R.string.nd_schedule_event_add);
		dlDrawer.addItem(new Item(AddClassEventFragment.class, temp, R.drawable.ic_action_new_event));

		temp = getString(R.string.nd_social_header);
		dlDrawer.addItem(new Header(temp));

		temp = getString(R.string.nd_social_friends);
		dlDrawer.addItem(new Item(FriendsFragment.class, temp, R.drawable.ic_action_person));

		temp = getString(R.string.nd_social_groups);
		dlDrawer.addItem(new Item(GroupsFragment.class, temp, R.drawable.ic_action_group));
		
		temp = getString(R.string.nd_social_books);
		dlDrawer.addItem(new Item(BooksFragment.class, temp, R.drawable.ic_action_books));

		temp = getString(R.string.nd_app_header);
		dlDrawer.addItem(new Header(temp));

		temp = getString(R.string.nd_app_settings);
		dlDrawer.addItem(new Item(SettingsFragment.class, temp, R.drawable.ic_action_settings));

		temp = getString(R.string.nd_app_logout);
		dlDrawer.addItem(new Item(UnderConstructionFragment.class, temp, R.drawable.ic_action_back));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(Constants.INTENT_KEY_SELECTEDITEMPOS, dlDrawer.getSelectedItemPos());
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
