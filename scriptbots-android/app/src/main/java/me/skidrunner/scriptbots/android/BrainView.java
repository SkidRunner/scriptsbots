/*
 * Copyright (C) 2016  Mark E. Picknell
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.skidrunner.scriptbots.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RemoteViews;

@RemoteViews.RemoteView
public class BrainView extends ViewGroup implements {
    private static final String TAG = "BrainView";
    private static final Logger mLogger = Logger.getLogger(BrainView.getName());

    private static final float PAN_VELOCITY_FACTOR = 2f;

    private static final float ZOOM_AMOUNT = 0.25f;

    private static final float AXIS_X_MIN = -1f;
    private static final float AXIS_X_MAX = 1f;
    private static final float AXIS_Y_MIN = -1f;
    private static final float AXIS_Y_MAX = 1f;

    private RectF mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

    private Rect mContentRect = new Rect();

    private ScaleGestureDetector mScaleDetector;
    private PointF mViewportFocus = new PointF();
    private float mScaleFactor = 1.f;

    // ********************************
    // Constructors
    // ********************************

    public BrainView(Context context) {
        this(context, null, 0, 0);
    }

    public BrainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public BrainView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public BrainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray themeStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BrainView, defStyle, defStyleRes);

        try {
            //TODO: load style attributes
        } catch (Exception e) {
            String message = "Could not load attributes";
            mLogger.log(Level.WARNING, message, e);
            Log.v(TAG, message, e);
        } finally {
            themeStyledAttributes.recycle();
        }

        mScaleDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);

    }

    // ********************************
    // Properties
    // ********************************

    // ********************************
    // ViewGroup Members
    // ********************************

    @Override
    public void draw(Canvas canvas) {
        // Manually render this view (and all of its children) to the given Canvas.

        // Clips the child drawing operations to the content area
        int saveCount = canvas.save();
        canvas.clipRect(mContentRect);

        //super.draw(canvas);

        Paint p = new Paint();
        p.setColor(Color.RED);
        boolean drawFirst = true;
        boolean drawRect = true;
        for (int y = 0; y < canvas.getHeight(); y += 10) {

            if (drawFirst) {
                canvas.drawRect(0, y, 10, y + 10, p);
            }

            drawRect = !drawFirst;

            for (int x = 10; x < canvas.getWidth(); x += 10) {
                if (drawRect) {
                    canvas.drawRect(0, y, 10, y + 10, p);
                }
                drawRect = !drawRect;
            }
            drewFirst = !drewFirst;
        }

        // Removes clipping rectangle
        canvas.restoreToCount(saveCount);

        // Draws chart container
        canvas.drawRect(mContentRect, mAxisPaint);
    }

    @Override
    boolean onTouchEvent(MotionEvent event) {
        // Implement this method to handle touch screen motion events.
        boolean handled = mScaleDetector.onTouchEvent(event);
        if (!handled) {
            handled = mGestureDetector.onTouchEvent(event);
        }
        if (!handled) {
            handled = super.onTouchEvent(event);
        }
        return handled;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Called from layout when this view should assign a size and position to each of its children.
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Measure the view and its content to determine the measured width and the measured height.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // Hook allowing a view to re-apply a representation of its internal state that had previously been generated by onSaveInstanceState().
        return super.onRestoreInstanceState(state);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Hook allowing a view to generate a representation of its internal state that can later be used to create a new instance with that same state.
        super.onSaveInstanceState();
    }

    // ********************************
    // OnGestureListener
    // ********************************

    @Override
    public boolean onDown(MotionEvent e) {
        // Notified when a tap occurs with the down MotionEvent that triggered it.
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Notified of a fling event when it occurs with the initial on down MotionEvent and the matching up MotionEvent.
        return false;
    }

    @Override
    public boolean onLongPress(MotionEvent e) {
        // Notified when a long press occurs with the initial on down MotionEvent that trigged it.
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Notified when a scroll occurs with the initial on down MotionEvent
        and the current move MotionEvent.
        return false;
    }

    @Override
    public boolean onShowPress(MotionEvent e) {
        // The user has performed a down MotionEvent and not performed a move or up yet.
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Notified when a tap occurs with the up MotionEvent that triggered it.
        return false;
    }

    // ********************************
    // OnDoubleTapListener
    // ********************************

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // Notified when a double-tap occurs.
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Notified when an event within a double-tap gesture occurs, including the down, move, and up events.
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // Notified when a single-tap occurs.
        return false;
    }

    // ********************************
    // OnScaleGestureListener
    // ********************************

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // Responds to scaling events for a gesture in progress.
        float newScaleFactor = mScaleFactor * detector.getScaleFactor();

        // Don't let the object get too small or too large.
        newScaleFactor = Math.max(0.1f, Math.min(newScaleFactor, 5.0f));

        float newWidth = newScaleFactor * mCurrentViewport.width();
        float newHeight = newScaleFactor * mCurrentViewport.height();

        float focusX = scaleGestureDetector.getFocusX();
        float focusY = scaleGestureDetector.getFocusY();

        float newLeft = mViewportFocus.x - newWidth * (focusX - mContentRect.left) / mContentRect.width();
        float newTop = viewportFocus.y - newHeight * (mContentRect.bottom - focusY) / mContentRect.height();
        float newRight = newLeft + newWidth;
        float newBottom = newTop + newHeight;

        mCurrentViewport.set(newLeft, newTop, newRight, newBottom);

        invalidate();

        mScaleFactor = newScaleFactor;

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // Responds to the beginning of a scaling gesture.
        return true;
    }

    @Override
    public void onScaleBegin(ScaleGestureDetector detector) {
        // Responds to the end of a scale gesture.
        return false;
    }

}