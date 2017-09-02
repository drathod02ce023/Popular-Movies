package com.example.android.popularmovies.adaptors;

import android.content.Context;
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

    public MovieDetailAdaptor(Context context,MovieDetailAdaptor.PlayButtonClickListener playButtonClickListener ) {
        this.context = context;
        this.mPlayButtonClickListener = playButtonClickListener;
    }
    /**
     * Cache of the children views for a movies list item.
     */
    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgPlayButton)
        ImageView imgPlayButton;
        @BindView(R.id.tvTrailer)
        TextView tvTrailer;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public MovieDetailAdaptor.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutid = R.layout.movie_trailer;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutid,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDetailAdaptor.TrailerViewHolder holder, final int position) {
        int TrailerNumber = position + 1; //Starts with 1 not 0
        String text = "Trailer " + Integer.toString(TrailerNumber);
        holder.tvTrailer.setText(text);
        holder.imgPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayButtonClickListener.onPlayButtonClick(movie.getLstVideo().get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movie == null){
            return 0;
        }
        return movie.getLstVideo().size();
    }

    /**
     * Event handler interface fro play button click
     */
    public interface PlayButtonClickListener{
        public void onPlayButtonClick(Video video);
    }

    /**
     * Pass data from Activity to adaptor.
     * @param movie
     */
    public void setData(Movie movie){
        this.movie = movie;
        notifyDataSetChanged();
    }
}
