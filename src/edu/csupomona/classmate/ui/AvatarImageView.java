package edu.csupomona.classmate.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AvatarImageView extends ImageView {
	private static final float RADIUS = 18f;

	public AvatarImageView(Context c) {
		super(null);
	}

	public AvatarImageView(Context c, AttributeSet attrs) {
		super(c, attrs);
	}

	public AvatarImageView(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clip = new Path();
		RectF rect = new RectF(0, 0, getWidth(), getHeight());
		clip.addRoundRect(rect, RADIUS, RADIUS, Path.Direction.CW);
		canvas.clipPath(clip);
		super.onDraw(canvas);
	}
}
