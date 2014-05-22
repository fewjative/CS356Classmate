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
import edu.csupomona.classmate.fragments.books.BuyBooksTab;
import edu.csupomona.classmate.fragments.books.SellBooksTab;
import edu.csupomona.classmate.fragments.books.AdminBooksTab;


public class BooksFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.books_fragment_layout, container, false);

		final ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		vpContentPane.setAdapter(new BooksFragmentPagerAdapter(getChildFragmentManager()));
		vpContentPane.setCurrentItem(0);
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

	private class BooksFragmentPagerAdapter extends FragmentPagerAdapter {
		BooksFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0: return new BuyBooksTab();
				case 1: return new SellBooksTab();
				case 2: return new AdminBooksTab();
				default: return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int i) {
			switch (i) {
				case 0: return getString(R.string.books_tab_buy);
				case 1: return getString(R.string.books_tab_sell);
				case 2: return getString(R.string.books_tab_admin);
				default: throw new RuntimeException();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
}
