package edu.csupomona.cs.cs356.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import edu.csupomona.cs.cs356.classmate.R;

public class TodaysFragment extends Fragment {
	private static final TodaysFragment INSTANCE = new TodaysFragment();

	public static TodaysFragment newInstance() {
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.todays_schedule_fragment, null);

		ImageButton btnAddClass = (ImageButton)root.findViewById(R.id.btnAddClass);
		btnAddClass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getActivity(), "You clicked me, ouch", Toast.LENGTH_LONG).show();
			}
		});

		return root;
	}
}