package kz.optimabank.optima24.utility.views;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import kz.optimabank.optima24.utility.ContactlessUtils;

import static kz.optimabank.optima24.utility.Utilities.getPreferences;


public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {
    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private float downRawX, downRawY;
    private float dX, dY;
    Context context;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    AppBarLayout appBarLayout;

    boolean vibrate = false;

    public MovableFloatingActionButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        sharedPreferences = getPreferences(context);
        setOnTouchListener(this);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setAppBarLayout(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;
            vibrate = false;
            return true; // Consumed

        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!vibrate) {
                vibrate = true;
                recyclerView.setNestedScrollingEnabled(false);
                appBarLayout.setExpanded(true, true);
                ContactlessUtils.vibrate(context, 200);
            }
            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            View viewParent = (View)view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true;

        } else if (action == MotionEvent.ACTION_UP) {
            recyclerView.setNestedScrollingEnabled(true);
            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                return performClick();
            } else { // A drag
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("pay_button_x",view.getTranslationX());
                editor.putFloat("pay_button_y",view.getTranslationY());
                editor.apply();
                return true; // Consumed
            }
        } else {
            return super.onTouchEvent(motionEvent);
        }

    }
}
