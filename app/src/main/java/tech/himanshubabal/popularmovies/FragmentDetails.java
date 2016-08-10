package tech.himanshubabal.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetails extends Fragment {
    @BindView(R.id.poster_image) ImageView posterImage;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_overview) TextView movieOverview;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.movie_year) TextView movieYear;

    private int db_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_about, container, false);
        ButterKnife.bind(this, view);

        posterImage.setImageBitmap(((MovieDetails)getActivity()).posterImageBitmap);
        movieTitle.setText(((MovieDetails)getActivity()).title);
        movieOverview.setText(((MovieDetails)getActivity()).overview);
        movieRating.setText(((MovieDetails)getActivity()).rating);
        movieYear.setText(((MovieDetails)getActivity()).releaseDate);
        db_id = ((MovieDetails)getActivity()).db_id;


        return view;
    }
}
