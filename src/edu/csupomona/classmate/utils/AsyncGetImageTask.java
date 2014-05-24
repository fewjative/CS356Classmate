package edu.csupomona.classmate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class AsyncGetImageTask extends AsyncTask<String, Void, Bitmap> {
	private final ImageView IMAGEVIEW;
	private final LinearLayout PROGRESS_SPINNER;

	public AsyncGetImageTask(ImageView iv) {
		this(iv, null);
	}

	public AsyncGetImageTask(ImageView iv, LinearLayout llProgressSpinner) {
		this.IMAGEVIEW = iv;
		this.PROGRESS_SPINNER = llProgressSpinner;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap image = null;
		InputStream is = null;
		try {
			is = new URI(params[0]).toURL().openStream();
			image = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}

		return image;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			IMAGEVIEW.setImageBitmap(result);
			if (PROGRESS_SPINNER != null) {
				PROGRESS_SPINNER.setVisibility(View.GONE);
			}
		}
	}
}
