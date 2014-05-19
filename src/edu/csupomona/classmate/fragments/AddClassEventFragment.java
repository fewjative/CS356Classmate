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
import edu.csupomona.classmate.fragments.addclassevent.AddClassTab;
import edu.csupomona.classmate.fragments.addclassevent.AddEventTab;
import edu.csupomona.classmate.fragments.addclassevent.SearchEventTab;
import edu.csupomona.classmate.fragments.addclassevent.CreateEventTab;

public class AddClassEventFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.viewpager_tab_layout, container, false);

		final ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new AddClassEventFragmentPagerAdapter(getChildFragmentManager()));
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

	private class AddClassEventFragmentPagerAdapter extends FragmentPagerAdapter {
		AddClassEventFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0: return new AddClassTab();
				case 1: return new AddEventTab();
				case 2: return new SearchEventTab();
				case 3: return new CreateEventTab();
				default: return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return getString(R.string.add_class_event_addclass);
				case 1: return getString(R.string.add_class_event_addevent);
				case 2: return getString(R.string.add_class_event_search_event);
				case 3: return getString(R.string.add_class_event_create_event);
				default: throw new RuntimeException();
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	}
}
