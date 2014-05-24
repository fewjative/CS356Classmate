package edu.csupomona.classmate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.NewsArticle;

public class NewsArticleActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_article_activity_layout);

		NewsArticle article = getIntent().getParcelableExtra(Constants.INTENT_KEY_NEWSARTICLE);
		getActionBar().setTitle(article.getTitle());

		WebView wvContentPane = (WebView)findViewById(R.id.wvContentPane);
		wvContentPane.loadUrl(article.getArticleURL());
	}
}
