package edu.csupomona.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.fragments.friends.FriendRequestsTab;
import edu.csupomona.classmate.fragments.friends.FriendsListTab;

public class GroupsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.groups_fragment_layout, container, false);

		ViewPager vpContentPane = (ViewPager)root.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new GroupsFragmentPagerAdapter(getChildFragmentManager()));

		return root;
	}

	private static class GroupsFragmentPagerAdapter extends FragmentPagerAdapter {
		GroupsFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0: return new FriendsListTab();
				case 1: return new FriendRequestsTab();
				default: return null;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return "My Friends";
				case 1: return "Requests";
				default: return "null";
			}
		}
	}
}
