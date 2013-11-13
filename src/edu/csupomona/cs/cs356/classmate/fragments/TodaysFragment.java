package edu.csupomona.cs.cs356.classmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;

public class TodaysFragment extends Fragment {
	private static final TodaysFragment INSTANCE = new TodaysFragment();
	private int userID;
	public static TodaysFragment newInstance() {
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.todays_schedule_fragment, null);
		userID = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, -1);
		if(userID == -1){
			return root;
		}
		ImageButton btnAddClass = (ImageButton)root.findViewById(R.id.btnAddClass);
		btnAddClass.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), AddClassActivity.class);
				i.putExtra("userID", userID);
				startActivityForResult(i,1);
			}
		});

		return root;
	}
}