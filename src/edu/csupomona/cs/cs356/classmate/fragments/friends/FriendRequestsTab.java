package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.List;

public class FriendRequestsTab extends ListFragment {
	private List<Friend> friends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_requests, null);
		//return root;

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		View rowView  = inflater.inflate(R.layout.tab_friends_requests, container, false);

		return rootView;
	}
}