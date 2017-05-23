package com.example.android.mypopularmovies.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.model.Review;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the adapter that is in charge to inflate each review layout and fill with the information received from the movie server.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolderReview> {

    private WeakReference<Activity> mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Activity mContext, List<Review> reviewList) {
        this.mContext = new WeakReference<>(mContext);
        setData(reviewList);
    }

    @Override
    public ViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolderReview(inflater.inflate(R.layout.review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderReview viewHolder, int position) {
        Review review = mReviewList.get(position);
        if (review != null) {
            viewHolder.reviewAuthor.setText(review.getAuthor());
            viewHolder.reviewContent.setText(review.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return (mReviewList != null) ? mReviewList.size() : 0;
    }

    public class ViewHolderReview extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author)
        TextView reviewAuthor;
        @BindView(R.id.review_content)
        TextView reviewContent;

        public ViewHolderReview(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<Review> reviewList) {
        if (reviewList != null) {
            mReviewList = new ArrayList<>(reviewList);
            notifyDataSetChanged();
        }
    }
}
