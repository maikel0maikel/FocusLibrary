package com.sinohb.lib.keyeventhandle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

public class ShadowDrawable extends Drawable {
    private Paint mPaint;
    private View view;
    private Paint mPaintOut;
    Bitmap bd;
    private RectF mRectf;
    //Bitmap lightBg;
    Drawable mDrawable;
    private ShadowDrawable(View view) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5.0f);
//        mPaint.setStyle(Paint.Style.FILL);

//        mPaintOut = new Paint();
//        mPaintOut.setAntiAlias(true);
//        mPaintOut.setDither(true);
//        mPaintOut.setStyle(Paint.Style.FILL);

//        mPaintOut.setColor(Color.YELLOW);
        this.view = view;
       view.setDrawingCacheEnabled(true);
        bd = view.getDrawingCache();
       // bd = loadBitmapFromView(view);
        //view.setDrawingCacheEnabled(false);
        //lightBg = BitmapFactory.decodeResource(view.getContext().getResources(), R.mipmap.item_press_focus);
        mRectf = new RectF(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
//        mRectf = new RectF(left -15, top - 15, view.getMeasuredWidth() + right + 15,
//                view.getMeasuredHeight() + bottom +15);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        // canvas.drawRect(new Rect(10, 10, view.getMeasuredWidth() - 10, view.getMeasuredHeight() - 10), mPaintOut);

        //canvas.drawRoundRect(mRectf,10,10, mPaint);
//        if (lightBg == null) {

//        } else {
//            canvas.drawBitmap(lightBg, view.getLeft() - 10, view.getTop() - 10, mPaint);


        canvas.drawRoundRect(mRectf, 8, 8, mPaint);

        mPaint.reset();
        mPaint.setColor(Color.parseColor("#00CED1"));
        mPaint.setStyle(Paint.Style.FILL);
        if (bd != null) {
            canvas.save();
            canvas.scale(0.8f, 0.8f,view.getMeasuredWidth()/2,view.getMeasuredHeight()/2);
            //canvas.translate(view.getMeasuredWidth()*0.2f, view.getMeasuredHeight()*0.2f);
            canvas.drawBitmap(bd, 0, 0, mPaint);
            //view.setDrawingCacheEnabled(false);
        }
        //canvas.restore();

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap loadBitmapFromView(View v) {

        int w = v.getWidth();

        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bmp);

        c.drawColor(Color.parseColor("#63B8FF"));

        v.layout(0, 0, w, h);

        v.draw(c);

        return bmp;

    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static void setShadowDrawable(View view, Drawable drawable) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view) {
        ShadowDrawable drawable = new ShadowDrawable(view);

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view.setBackground(null);
        if (view instanceof ImageView){
            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(drawable);
        }else {
            view.setBackground(drawable);
        }
        //  ViewCompat.setBackground(view, drawable);
    }
}
