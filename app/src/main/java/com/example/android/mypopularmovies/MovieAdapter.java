package com.example.android.mypopularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ltorres on 10/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mData;
    private Context mContext;
    private ItemClickListener mClickListener;

    public MovieAdapter(Context context, List<Movie> data) {
        this.mData = data;
        this.mContext = context;
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

    private void configureViewHolderMovie(ViewHolderMovie holder, int position) {
        Movie movie = mData.get(position);
        if (movie != null) {
            Picasso.with(mContext).load(movie.getPoster_path()).placeholder(R.mipmap.ic_launcher).into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public Movie getItem(int position) {
        if (mData != null && mData.size() > 0)
            return mData.get(position);
        return null;
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView poster;

        public ViewHolderMovie(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
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

    public void setData(List<Movie> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void setMoreMovies(List<Movie> newMovies) {
        if (newMovies != null && newMovies.size() > 0) {
            mData.addAll(newMovies);
            notifyDataSetChanged();
        }
    }
}



