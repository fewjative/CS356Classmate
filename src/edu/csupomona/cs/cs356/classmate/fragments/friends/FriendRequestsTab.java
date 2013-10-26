package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.csupomona.cs.cs356.classmate.R;

public class FriendRequestsTab extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_requests, null);
		return root;
	}
}
