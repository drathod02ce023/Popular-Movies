package com.example.android.popularmovies.adaptors;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhaval on 2017/09/02.
 * To display movie trailers
 */

public class MovieDetailAdaptor extends RecyclerView.Adapter<MovieDetailAdaptor.TrailerViewHolder> {

    private Movie movie;
    final private Context context;
    final private MovieDetailAdaptor.PlayButtonClickListener mPlayButtonClickListener;
    private static final int VIEW_TRAILER = 1;
    private static final int VIEW_REVIEW = 2;
    private int reviewno = 0;

    public MovieDetailAdaptor(Context context,MovieDetailAdaptor.PlayButtonClickListener playButtonClickListener ) {
        this.context = context;
        this.mPlayButtonClickListener = playButtonClickListener;
    }
    /**
     * Cache of the children views for a movies list item.
     */
    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.imgPlayButton)
        ImageView imgPlayButton;
        @Nullable
        @BindView(R.id.tvTrailer)
        TextView tvTrailer;
        @Nullable
        @BindView(R.id.tvReview)TextView tvReview;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        /**
         * Bind data and events to view holder controls
         * @param position
         * @param viewId
         */
        private void bind(final int position,int viewId){
            int TrailerNumber = position + 1; //Starts with 1 not 0
            String text = "Trailer " + Integer.toString(TrailerNumber);

            switch (viewId){
                case VIEW_TRAILER:{
                    tvTrailer.setText(text);
                    imgPlayButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPlayButtonClickListener.onPlayButtonClick(movie.getLstVideo().get(position));
                        }
                    });
                    break;
                }
                case VIEW_REVIEW:{
                    if(movie.getLstReview().size() > reviewno) {
                        tvReview.setText(movie.getLstReview().get(reviewno).getReviewContent());
                        reviewno = reviewno + 1;
                    }
                }
            }

        }
    }

    @Override
    public MovieDetailAdaptor.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutid;
        switch(viewType){
            case VIEW_TRAILER:{
                layoutid = R.layout.movie_trailer;
                break;
            }
            case VIEW_REVIEW:{
                layoutid = R.layout.movie_reviews;
                break;
            }
            default:{
                throw new UnsupportedOperationException("View not supported");
            }
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutid,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDetailAdaptor.TrailerViewHolder holder, final int position) {

        holder.bind(position,getItemViewType(position));

    }

    @Override
    public int getItemCount() {
        if(movie == null){
            return 0;
        }
        return movie.getLstVideo().size() + movie.getLstReview().size();
    }

    /**
     * Event handler interface fro play button click
     */
    public interface PlayButtonClickListener{
         void onPlayButtonClick(Video video);
    }

    /**
     * Pass data from Activity to adaptor.
     * @param movie
     */
    public void setData(Movie movie){
        this.movie = movie;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < movie.getLstVideo().size()){
            return VIEW_TRAILER;
        }
        else{
            return VIEW_REVIEW;
        }
    }
}
