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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the adapter that is in charge to inflate each trailer layout and fill with the information received from the movie server. It also handles
 * click listener and deliver it to Youtube.
 */
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

        @BindView(R.id.trailer_img)
        ImageView trailerImg;
        @BindView(R.id.trailer_text)
        TextView trailerText;

        public ViewHolderTrailer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            trailerImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = mTrailerList.get(getAdapterPosition());
            if (trailer != null) {
                //redirect user to youtube given a traibler path
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
