package edu.csupomona.classmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.NewsArticle;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsFeedFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.http_query_listview_layout, null);

		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETNEWSFEED, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<NewsArticle> articles = new LinkedList<NewsArticle>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						articles.add(new NewsArticle(
							jObj.getString(Constants.PHP_PARAM_DATE),
							jObj.getString(Constants.PHP_PARAM_TITLE),
							jObj.getString(Constants.PHP_PARAM_DESC),
							jObj.getString(Constants.PHP_PARAM_IMAGEURL),
							jObj.getString(Constants.PHP_PARAM_ARTICLEURL)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				final NewsFeedAdapter adapter = new NewsFeedAdapter(getActivity(), articles);
				ListView lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(R.string.activity_feed_empty);

					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvQueryResults.setAdapter(adapter);
					lvQueryResults.setDivider(null);
					lvQueryResults.setBackgroundResource(R.color.white_smoke);
					lvQueryResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							NewsArticle article = adapter.getItem(position);

							Intent i = new Intent(NewsFeedFragment.this.getActivity(), NewsArticleActivity.class);
							i.putExtra(Constants.INTENT_KEY_NEWSARTICLE, article);
							startActivity(i);
						}
					});
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}
