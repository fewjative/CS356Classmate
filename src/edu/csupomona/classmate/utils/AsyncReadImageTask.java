package edu.csupomona.classmate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import edu.csupomona.classmate.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class AsyncReadImageTask extends AsyncTask<String, Void, Bitmap> {
	private final ImageView IMAGEVIEW;

	public AsyncReadImageTask(ImageView iv) {
		this.IMAGEVIEW = iv;
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
			Animation animFadeIn = AnimationUtils.loadAnimation(IMAGEVIEW.getContext(), R.anim.user_avatar_fadein);
			IMAGEVIEW.startAnimation(animFadeIn);
			IMAGEVIEW.setImageBitmap(result);
		}
	}
}
