package com.example.android.popularmovies.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by dhaval on 2017/08/09.
 */

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MoviesViewHolder> {

    private String[] posterArray;

    public MoviesAdaptor(String[] posterArray) {
        this.posterArray = posterArray;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
        String posterUrl = posterArray[position];
        Picasso.with(holder.imgPoster.getContext()).load(posterUrl).into(holder.imgPoster);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if(posterArray != null) {
            return posterArray.length;
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
     * @param p_PosterArray
     */
    public void setData(String[] p_PosterArray){
        posterArray = p_PosterArray;
        notifyDataSetChanged();
    }

}
