package gleroy.com.mybaseapplication.presentation.adapter.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalItemDecorator extends RecyclerView.ItemDecoration {

    private final int mSpaceHeight;

    /**
     * Flag to know if we want to add a padding at the top of the RecyclerView
     */
    private boolean mAddTopPadding;

    public HorizontalItemDecorator(int spaceHeight) {
        mSpaceHeight = spaceHeight;
        mAddTopPadding = true;
    }

    public HorizontalItemDecorator(int spaceHeight, boolean addTopPadding) {
        mSpaceHeight = spaceHeight;
        mAddTopPadding = addTopPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mSpaceHeight;

        // Add top margin only for the first item to avoid double space between items
        if (mAddTopPadding && parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mSpaceHeight;
        } else {
            outRect.top = 0;
        }
    }
}
