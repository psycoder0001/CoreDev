package com.deepdroid.coredev.customview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrenozturk on 11/20/14.
 */
public abstract class CoreDevListWithoutScroll extends LinearLayout {

    private final List<View> mLayoutItems = new ArrayList<View>();
    private List<?> mDataItems = new ArrayList<>();

    public CoreDevListWithoutScroll(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public CoreDevListWithoutScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreDevListWithoutScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean inflateViews(Context context, final List<?> objects, int sourceLayoutId) {
        return inflateViews(context, objects, sourceLayoutId, 0, true);
    }

    public boolean inflateViews(Context context, final List<?> objects, int sourceLayoutId, int separatorSize) {
        return inflateViews(context, objects, sourceLayoutId, separatorSize, true);
    }

    public boolean inflateViews(Context context, final List<?> objects, int sourceLayoutId, int separatorSize, boolean layoutAnimationEnabled) {

        if (layoutAnimationEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayoutTransition(new LayoutTransition());
        }

        if (objects == null || objects.size() < 1) {
            return false;
        }

//        mDataItems = (List<?>) objects.clone();
        mDataItems = objects;

        LayoutInflater inflater = LayoutInflater.from(context);

        removeAllViews();
        mLayoutItems.clear();
        for (int x = 0; x < mDataItems.size(); x++) {

            final View view = inflater.inflate(sourceLayoutId, this, false);
            if (getOrientation() == LinearLayout.VERTICAL) {
                view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (separatorSize != 0 && x < mDataItems.size() - 1) {
                    ((LayoutParams) view.getLayoutParams()).bottomMargin = separatorSize;
                }
            } else {
                view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (separatorSize != 0 && x < mDataItems.size() - 1) {
                    ((LayoutParams) view.getLayoutParams()).rightMargin = separatorSize;
                }
            }
            setDataAt(x, view, mDataItems.get(x));
            addView(view);

            final int position = x;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
            mLayoutItems.add(view);
        }

        return true;
    }

    public void notifyDataSetChanged() {
        if (mDataItems != null) {
            for (int x = 0; x < mDataItems.size(); x++) {
                if (x < mLayoutItems.size()) {
                    setDataAt(x, mLayoutItems.get(x), mDataItems.get(x));
                }
            }
        }
    }

    protected abstract void setDataAt(int index, View convertView, Object viewData);

    // ==================
    // Get Items
    // ==================
    public <T> List<T> getDataItems() {
        try {
            return (List<T>) mDataItems;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getDataItemAt(int index) {
        if (mDataItems != null && index < mDataItems.size()) {
            try {
                return (T) mDataItems.get(index);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public View getLayoutItemAt(int index) {
        if (index > -1 && index < mLayoutItems.size()) {
            return mLayoutItems.get(index);
        }
        return null;
    }

    public <T> T getViewHolderAt(int index) {
        T tagObjectT = null;
        if (index > -1 && index < mLayoutItems.size()) {
            Object tagObject = mLayoutItems.get(index).getTag();
            if (tagObject != null) {
                try {
                    tagObjectT = (T) tagObject;
                } catch (ClassCastException ignored) {
                    // Wrong class type.
                }
            }
        }
        return tagObjectT;
    }

    public int getItemCount() {
        if (mLayoutItems == null) {
            return 0;
        }
        return mLayoutItems.size();
    }

    // ==================
    // OnItemClick
    // ==================

    protected abstract void onItemClick(View view, int index);
}
