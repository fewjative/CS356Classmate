package edu.csupomona.classmate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.NewsArticle;
import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter<NewsArticle> {
	public NewsFeedAdapter(Context context, List<NewsArticle> articles) {
		super(context, 0, articles);
	}

	private static class ViewHolder {
		final ImageView ivImage;
		final TextView tvTitle;
		final TextView tvDesc;
		final TextView tvDate;
		final LinearLayout llProgressBar;

		ViewHolder(ImageView ivImage, TextView tvTitle, TextView tvDesc, TextView tvDate, LinearLayout llProgressBar) {
			this.ivImage = ivImage;
			this.tvTitle = tvTitle;
			this.tvDesc = tvDesc;
			this.tvDate = tvDate;
			this.llProgressBar = llProgressBar;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsArticle article = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		//if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.news_article_item_layout, null);

			ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
			TextView tvDesc = (TextView)view.findViewById(R.id.tvDesc);
			TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
			LinearLayout llProgressBar = (LinearLayout)view.findViewById(R.id.llProgressBar);
			view.setTag(new ViewHolder(ivImage, tvTitle, tvDesc, tvDate, llProgressBar));
		//}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (article != null && holder != null) {
			if (holder.ivImage != null) {
				article.loadImage(holder.ivImage, holder.llProgressBar);
			}

			if (holder.tvTitle != null) {
				holder.tvTitle.setText(article.getTitle());
			}

			if (holder.tvDesc != null) {
				holder.tvDesc.setText(article.getDesc());
			}

			if (holder.tvDate != null) {
				holder.tvDate.setText(article.getDate());
			}
		}

		return view;
	}
}
