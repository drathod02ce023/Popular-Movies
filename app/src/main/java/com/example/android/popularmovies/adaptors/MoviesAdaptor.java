package com.example.android.popularmovies.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utility.MoviesDBUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhaval on 2017/08/09.
 * RecyclerView's Adaptor to load movie list data.
 */

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MoviesViewHolder> {

    private List<Movie> lstMovies;
    final private Context context;
    final private MovieOnClickListener mMovieOnClickListener;

    public MoviesAdaptor(Context context, MovieOnClickListener movieOnClickListener) {

        this.context = context;
        this.mMovieOnClickListener = movieOnClickListener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details.
     * @return A new MoviesViewHolder that holds the View for each list item
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new MoviesViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = lstMovies.get(position);
        String posterPath = movie.getPosterPath();
        String fullPosterPath = MoviesDBUtil.getPosterURL(posterPath, context);
        Picasso.with(context).load(fullPosterPath).placeholder(R.mipmap.ic_launcher).into(holder.imgPoster);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (lstMovies != null) {
            return lstMovies.size();
        }
        return 0;
    }

    /**
     * Cache of the children views for a movies list item.
     */
    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgPoster)
        ImageView imgPoster;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie movie = lstMovies.get(getAdapterPosition());
            mMovieOnClickListener.onMovieClickEvent(movie);
        }
    }

    /**
     * Interface to implement click event of movie poster.
     */
    public interface MovieOnClickListener {
        void onMovieClickEvent(Movie movie);
    }

    /**
     * This method is to set/change adaptor data and notify adaptor about the same.
     *
     * @param lstMovies List of movies fetched from APIs.(To be seen on the screen.)
     */
    public void setData(List<Movie> lstMovies) {
        this.lstMovies = lstMovies;
        notifyDataSetChanged();
    }

}
