package com.example.android.mypopularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.util.ImageUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the adapter that is in charge to inflate each movie layout and fill with the information received from the movie server. It also handles
 * click listener and deliver it to MainActivity.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mData;
    private int mImageWidth, mImageHeight;
    private ItemClickListener mClickListener;

    public MovieAdapter(Context context, ArrayList<Movie> data) {
        this.mData = data;
        this.mContext = context;
        int[] heightAndWidth = ImageUtil.getPosterHeightAndWidth((Activity) context);
        mImageHeight = heightAndWidth[0];
        mImageWidth = heightAndWidth[1];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.movie_item, parent, false);
        return new ViewHolderMovie(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        configureViewHolderMovie((ViewHolderMovie) viewHolder, position);
    }

    /**
     * Loads the movie poster from Picasso
     *
     * @param holder
     * @param position
     */
    private void configureViewHolderMovie(ViewHolderMovie holder, int position) {
        Movie movie = mData.get(position);
        if (movie != null) {
            Picasso.with(mContext).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    /**
     * Return a movie at specific position
     *
     * @param position
     * @return
     */
    public Movie getItem(int position) {
        if (mData != null && mData.size() > 0) {
            return mData.get(position);
        }
        return null;
    }

    /**
     * Return all movies stored at adapter
     *
     * @return
     */
    public ArrayList<Movie> getItems() {
        return mData;
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.poster)
        ImageView poster;

        public ViewHolderMovie(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            android.view.ViewGroup.LayoutParams layoutParams = poster.getLayoutParams();
            layoutParams.width = mImageWidth;
            layoutParams.height = mImageHeight;
            poster.setLayoutParams(layoutParams);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Sets a list of movies to be displayed at UI
     *
     * @param mData
     */
    public void setData(ArrayList<Movie> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    /**
     * Sets more movies when user is scrolling the list
     *
     * @param newMovies
     */
    public void setMoreMovies(List<Movie> newMovies) {
        if (newMovies != null && newMovies.size() > 0) {
            mData.addAll(newMovies);
            notifyDataSetChanged();
        }
    }
}



