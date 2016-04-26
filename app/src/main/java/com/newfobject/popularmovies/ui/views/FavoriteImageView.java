package com.newfobject.popularmovies.ui.views;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;


public class FavoriteImageView extends ImageView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };
    int squareDim = 1000000000;
    private boolean mChecked;

    public FavoriteImageView(Context context) {
        super(context);
    }

    public FavoriteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
        int curSquareDim = Math.min(w, h);
        if (curSquareDim < squareDim) {
            squareDim = curSquareDim;
        }
        setMeasuredDimension(squareDim, squareDim);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState result = new SavedState(super.onSaveInstanceState());
        result.checked = mChecked;
        return result;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setChecked(ss.checked);
    }

    protected static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        protected boolean checked;

        protected SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(checked ? 1 : 0);
        }
    }
}
