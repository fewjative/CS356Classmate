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

public class FriendsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.groups_fragment_layout, container, false);

		ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new FriendsFragmentPagerAdapter(getChildFragmentManager()));

		return ROOT;
	}

	private class FriendsFragmentPagerAdapter extends FragmentPagerAdapter {
		FriendsFragmentPagerAdapter(FragmentManager fm) {
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
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return getString(R.string.friends_tab_list);
				case 1: return getString(R.string.friends_tab_requests);
				default: throw new RuntimeException();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
