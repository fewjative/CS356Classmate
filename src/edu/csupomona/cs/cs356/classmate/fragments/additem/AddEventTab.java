package edu.csupomona.cs.cs356.classmate.fragments.additem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.INTENT_KEY_USERID;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEventTab extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_add, null);

		return root;
	}
}