package com.example.android.mypopularmovies.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * This is the adapter that check when is time to ask more items to movie server. It has a threshold and when the last visible item + the threshold are bigger than
 * total items available, is send a request to MainActivity in order to request more movies.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int mVisibleThreshold;

    private int mCurrentPage = 0;

    private int mPreviousTotalItemCount = 0;

    private boolean mLoading = true;

    private int mStartingPageIndex = 0;

    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        int totalItemCount;
        int lastVisibleItemPosition;

        totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        if (totalItemCount < mPreviousTotalItemCount) {
            mCurrentPage = mStartingPageIndex;
            mPreviousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.mLoading = true;
            }
        }

        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
            mCurrentPage++;
            onLoadMore(mCurrentPage, totalItemCount);
            mLoading = true;
        }
    }

    /**
     * Reset the scroll to the beginning. Used when user change the filer.
     */
    public void resetState() {
        mCurrentPage = mStartingPageIndex;
        mPreviousTotalItemCount = 0;
        mLoading = true;
    }

    public abstract void onLoadMore(int page, int totalItemCount);

    /**
     * Sets the visibleThreshold for a list
     *
     * @param visibleThreshold
     */
    public void setVisibleThreshold(int visibleThreshold) {
        mVisibleThreshold = visibleThreshold;
    }
}
