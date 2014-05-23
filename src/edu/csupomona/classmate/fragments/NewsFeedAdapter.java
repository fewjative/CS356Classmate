package edu.csupomona.classmate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

		ViewHolder(ImageView ivImage, TextView tvTitle, TextView tvDesc, TextView tvDate) {
			this.ivImage = ivImage;
			this.tvTitle = tvTitle;
			this.tvDesc = tvDesc;
			this.tvDate = tvDate;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsArticle article = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.news_article_item_layout, null);

			ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
			TextView tvDesc = (TextView)view.findViewById(R.id.tvDesc);
			TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
			view.setTag(new ViewHolder(ivImage, tvTitle, tvDesc, tvDate));
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (article != null && holder != null) {
			if (holder.ivImage != null) {
				article.loadImage(holder.ivImage);
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
