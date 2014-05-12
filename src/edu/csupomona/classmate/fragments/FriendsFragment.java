package edu.csupomona.classmate.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.fragments.friends.FriendRequestsTab;
import edu.csupomona.classmate.fragments.friends.MyFriendsTab;
import edu.csupomona.classmate.fragments.friends.SearchUsersTab;

public class FriendsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.viewpager_tab_layout, container, false);

		final ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new FriendsFragmentPagerAdapter(getChildFragmentManager()));
		vpContentPane.setCurrentItem(1);
		vpContentPane.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			private int previous;
			@Override
			public void onPageSelected(int position) {
				if (previous == 2 && previous != position) {
					InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(vpContentPane.getWindowToken(), 0);
					ROOT.clearFocus();
				}

				previous = position;
			}
		});

		return ROOT;
	}

	private class FriendsFragmentPagerAdapter extends FragmentPagerAdapter {
		FriendsFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0: return new FriendRequestsTab();
				case 1: return new MyFriendsTab();
				case 2: return new SearchUsersTab();
				default: return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return getString(R.string.friends_tab_requests);
				case 1: return getString(R.string.friends_tab_list);
				case 2: return getString(R.string.friends_tab_search);
				default: throw new RuntimeException();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
}
