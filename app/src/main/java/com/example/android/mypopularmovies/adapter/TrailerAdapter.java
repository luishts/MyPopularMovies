package com.example.android.mypopularmovies.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.model.Trailer;
import com.example.android.mypopularmovies.util.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolderTrailer> {

    private WeakReference<Activity> mContext;
    private List<Trailer> mTrailerList;

    public TrailerAdapter(Activity mContext, List<Trailer> trailerList) {
        this.mContext = new WeakReference<>(mContext);
        setData(trailerList);
    }

    @Override
    public ViewHolderTrailer onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolderTrailer(inflater.inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderTrailer viewHolder, int position) {
        Trailer trailer = mTrailerList.get(position);
        if (trailer != null) {
            viewHolder.trailerText.setText(trailer.getName());
        }
    }

    @Override
    public int getItemCount() {
        return (mTrailerList != null) ? mTrailerList.size() : 0;
    }

    public class ViewHolderTrailer extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailerImg;
        TextView trailerText;

        public ViewHolderTrailer(View itemView) {
            super(itemView);
            trailerImg = (ImageView) itemView.findViewById(R.id.trailer_img);
            trailerText = (TextView) itemView.findViewById(R.id.trailer_text);
            trailerImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = mTrailerList.get(getAdapterPosition());
            if (trailer != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYoutubeUri(trailer.getPath()));
                mContext.get().startActivity(intent);
            }
        }
    }

    public void setData(List<Trailer> trailerList) {
        if (trailerList != null) {
            mTrailerList = new ArrayList<>(trailerList);
            notifyDataSetChanged();
        }
    }
}
