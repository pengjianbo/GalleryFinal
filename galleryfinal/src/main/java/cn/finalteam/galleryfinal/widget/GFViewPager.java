package cn.finalteam.galleryfinal.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/29 0029 19:29
 */
public class GFViewPager extends ViewPager{
    public GFViewPager(Context context) {
        super(context);
    }

    public GFViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super .dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ignored) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return false ;
    }
}