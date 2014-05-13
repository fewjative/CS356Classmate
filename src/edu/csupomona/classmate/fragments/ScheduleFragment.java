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
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.schedule.DailyScheduleTab;
import edu.csupomona.classmate.fragments.schedule.WeeklyScheduleTab;

public class ScheduleFragment extends Fragment {
	private final User VIEWER;

	public ScheduleFragment() {
		super();
		this.VIEWER = null;
	}

	public ScheduleFragment(User viewer) {
		super();
		this.VIEWER = viewer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.viewpager_tab_layout, container, false);

		final ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new ScheduleFragmentPagerAdapter(getChildFragmentManager()));
		vpContentPane.setCurrentItem(0);
		vpContentPane.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			private int previous;
			@Override
			public void onPageSelected(int position) {
				if (previous == 2 && previous != position) {
					InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(vpContentPane.getWindowToken(), 0);
				}

				previous = position;
			}
		});

		return ROOT;
	}

	private class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {
		ScheduleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0: return new DailyScheduleTab(VIEWER);
				case 1: return new WeeklyScheduleTab(VIEWER);
				default: return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return getString(R.string.schedule_tab_daily);
				case 1: return getString(R.string.schedule_tab_weekly);
				default: throw new RuntimeException();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
