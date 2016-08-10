package tech.himanshubabal.popularmovies;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    Context context;
    List<MovieClass> movieClassList;
    OnItemClickListener onItemClickListener;
    MovieDBHelper movieDBHelper;

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_grid_imageView) ImageView imageView;
        @BindView(R.id.fav_icon_grid_photo) ImageView favIconImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
            favIconImageView.setOnClickListener(this);
            //Log.i("bitmap", "$");
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null){
                if ( view.getId() != R.id.fav_icon_grid_photo) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
                else {
                    onItemClickListener.onIconClick(favIconImageView, getAdapterPosition());
                }
            }
        }
    }

    public MovieAdapter (Context context, List<MovieClass> movieClassList, MovieDBHelper movieDBHelper) {
        this.context = context;
        this.movieClassList = movieClassList;
        this.movieDBHelper = movieDBHelper;
        //Log.i("bitmap", "%");
    }

    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
        public void onIconClick (ImageView view, int position);
    }

    public void setOnItemClickListener (final OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_grid_imageview, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //185 x 278 are the dimensions of requested image
        //342 x 513 --> new dimensions
        float imageRatio = (float)342/(float)513;     //img ratio is width/height

        //'scale' is used to convert DP to pixels
        final float scale = context.getResources().getDisplayMetrics().density;

        //  padding is the margin/padding kept from screen sides and between the images
        int padding = 0;
        //  2 * (width of final img) + 3 * (padding) = width of device screen.
        //  So,  width of final img = ((width of device) - 3 * (padding)) / 2
        int widthDp = (int) (dpWidth - 3 * padding) / 2;
        //  as to keep the width:height ratio same as original image
        //  height of final img = ratio * width of final img
        int heightDp = (int) (widthDp / imageRatio);
        //  final image dimensions are converted into pixels from DP
        //  so as to pass in picaso as inputs to resize image according to device.
        int widthPixels = (int) (widthDp * scale + 0.5f);
        int heightPixels = (int) (heightDp * scale + 0.5f);


        MovieClass movie = movieClassList.get(position);
        //Log.i("bitmap", movie.getDb_id());
        if (movieDBHelper.movieAlreadyExists(movie.getDb_id())) {
            holder.favIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favourite_filled));
        }
        else {
            holder.favIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_blank));
        }

        Picasso.with(context).load(movie.getPosterUrl()).placeholder(R.drawable.ic_theaters_white_18dp).resize(widthPixels, heightPixels).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        //Log.i("bitmap", "@");
        return movieClassList.size();
    }
}
