package edu.csupomona.cs.cs356.classmate.fragments;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawSquare extends View 
{
	private String color;
	private int numColor;
	private Bitmap shade;
	private Paint paint;
	private Random gen;
	public DrawSquare(Context mContext, String col, Resources r, int id) 
	{
        super(mContext);
        color = col;
        paint = new Paint();
        shade = BitmapFactory.decodeResource(r, id);
        gen = new Random();
    }

	public void onDraw(Canvas canvas) 
	{
		shade = Bitmap.createScaledBitmap(shade, canvas.getWidth(), canvas.getWidth(), true);
		
        paint.setStyle(Paint.Style.FILL);
//        canvas.drawColor(Color.parseColor(color)); // This is the correct implementation to use once the color variable is added to the database
        
        numColor = gen.nextInt(6) + 1;
        
        switch(numColor)
        {
        case 1:
        	canvas.drawColor(Color.parseColor("#1654FF")); // blue
        	break;
        case 2:
        	canvas.drawColor(Color.parseColor("#FAFF05")); // green
        	break;
        case 3:
        	canvas.drawColor(Color.parseColor("#DF1A1A")); // red
        	break;
        case 4:
        	canvas.drawColor(Color.parseColor("#FFA00E")); // orange
        	break;
        case 5:
        	canvas.drawColor(Color.parseColor("#00DC10")); // purple
        	break;
        case 6:
        	canvas.drawColor(Color.parseColor("#A205FF")); // yellow
        	break;
        	default:
        		canvas.drawColor(Color.WHITE);
        }
        canvas.drawBitmap(shade, 0,  0, null);
    }
}
