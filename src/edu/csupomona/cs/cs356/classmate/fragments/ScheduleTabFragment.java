package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.fragments.friends.AddFriendTab;
import edu.csupomona.cs.cs356.classmate.fragments.friends.FriendListTab;
import edu.csupomona.cs.cs356.classmate.fragments.friends.FriendRequestsTab;

public class ScheduleTabFragment extends Fragment {
	private FragmentTabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabHost = new FragmentTabHost(getActivity());
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.flTabContentPane);
		tabHost.addTab(tabHost.newTabSpec("dailyScheduleTab").setIndicator("Daily Schedule"), TodaysFragment.class, null);//DailyScheduleTab.class
		tabHost.addTab(tabHost.newTabSpec("weeklyScheduleTab").setIndicator("Weekly Schedule"), ScheduleFragment.class, null);//WeeklyScheduleTab.class
		return tabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost = null;
	}
}