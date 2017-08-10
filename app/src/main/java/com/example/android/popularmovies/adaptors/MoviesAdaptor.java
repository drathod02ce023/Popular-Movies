package com.example.android.popularmovies.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dhaval on 2017/08/09.
 */

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MoviesViewHolder> {

    private List<Movie> lstMovies;
    private Context context;

    public MoviesAdaptor(Context context) {
        this.context = context;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new MoviesViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = lstMovies.get(position);
        String posterPath = movie.getPosterPath();
        String fullPosterPath = ""; //TODO
        Picasso.with(context).load(fullPosterPath).into(holder.imgPoster);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if(lstMovies != null) {
            return lstMovies.size();
        }
        return 0;
    }

    /**
     * ViewHolder Class
     */
    public class MoviesViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPoster;
        public MoviesViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView)itemView.findViewById(R.id.imgPoster);
        }
    }

    /**
     * This method is to set/change adaptor data and notify adaptor about the same.
     * @param lstMovies
     */
    public void setData(List<Movie> lstMovies){
        this.lstMovies = lstMovies;
        notifyDataSetChanged();
    }

}
