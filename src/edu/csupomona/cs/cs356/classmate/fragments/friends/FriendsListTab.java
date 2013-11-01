package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;

public class FriendsListTab extends ListFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_list, null);
		//return root;

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		View rowView = inflater.inflate(R.layout.tab_friends_list, container, false);

		return rootView;
	}
}