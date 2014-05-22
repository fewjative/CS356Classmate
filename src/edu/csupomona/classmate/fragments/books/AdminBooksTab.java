package edu.csupomona.classmate.fragments.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.abstractions.Book;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminBooksTab extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.books_admin_tab_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETADMINBOOKLIST, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<Book> books = new LinkedList<Book>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						books.add(new Book(
							jObj.getLong(Constants.PHP_PARAM_BOOKLISTID),
							jObj.getLong(Constants.PHP_PARAM_USERID),
							jObj.getString(Constants.PHP_PARAM_BOOKTITLE),
							jObj.getString(Constants.PHP_PARAM_BOOKCLASSNAME),
							jObj.getString(Constants.PHP_PARAM_BOOKCONDITION),
							jObj.getString(Constants.PHP_PARAM_BOOKPRICE)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				AdminBookAdapter adapter = new AdminBookAdapter(getActivity(), USER, books);
				ListView lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(getResources().getString(R.string.book_list_empty, getResources().getString(R.string.books_tab_admin)));
					//^^CHANGE
					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvQueryResults.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}
