package tech.himanshubabal.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopMovies extends Fragment{
    RequestQueue requestQueue;
    @BindView(R.id.top_movies_recyclerView)
    RecyclerView recyclerView;
    MovieAdapter adapter;
    List<MovieClass> list = new ArrayList<>();
    MovieDBHelper movieDBHelper;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_movies, container, false);
        ButterKnife.bind(this, rootView);

        requestQueue = ((MainActivity) getActivity()).requestQueue;
        movieDBHelper = ((MainActivity)getActivity()).dbHelper;
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.popular_movies_recyclerView);

        //final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        adapter = new MovieAdapter(getActivity(), list, movieDBHelper);
        recyclerView.setAdapter(adapter);

        prepareList(1);

        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final MovieClass obj = list.get(position);
                VolleyCallback callback = new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) { }
                    String[] trailers;
                    String[] reviews;

                    @Override
                    public void onSuccessArrayResp_A(String[] result) {
                        reviews = result;
                    }

                    @Override
                    public void onSuccessArrayResp_B(String[] result) {
                        trailers = result;
//                        Bitmap poster = null;
//                        Bitmap backdrop = null;
//                        poster = DBUtility.getBitmapFromURL(obj.getPosterUrl());
//                        backdrop = DBUtility.getBitmapFromURL(obj.getBackdropURL());
//
//                        MovieDBObject object = new MovieDBObject(obj.getDb_id(), obj.getTitle(), obj.getOverview(),
//                                obj.getReleaseDate(), obj.getRating(), poster, backdrop, reviews, trailers);
//
//                        Log.i("database", obj.getDb_id() + obj.getTitle() + poster + backdrop);
//
//                        Intent intent = new Intent(getActivity(), MovieDetails.class);
//                        intent.putExtra("object", object);
//                        startActivity(intent);
                        MovieClass object = obj;
                        object.setReviews(reviews);
                        object.setTrailers(trailers);

                        Intent intent = new Intent(getActivity(), MovieDetails.class);
                        intent.putExtra("object", object);
                        startActivity(intent);

                    }
                };
                MainActivity.getObjectDetails(obj.getDb_id(), callback);
            }

            @Override
            public void onIconClick(ImageView view, final int position) {
                final MovieClass obj = list.get(position);
                Boolean alreadyInDB = movieDBHelper.movieAlreadyExists(obj.getDb_id());

                if (!alreadyInDB) { //Movie not present in DB
                    view.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.favourite_filled));

                    VolleyCallback callback = new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) { }
                        String[] trailers;
                        String[] reviews;

                        @Override
                        public void onSuccessArrayResp_A(String[] result) {
                            reviews = result;
                        }

                        @Override
                        public void onSuccessArrayResp_B(String[] result) {
                            trailers = result;
                            Bitmap poster = null;
                            Bitmap backdrop = null;
                            poster = DBUtility.getBitmapFromURL(obj.getPosterUrl());
                            backdrop = DBUtility.getBitmapFromURL(obj.getBackdropURL());

                            MovieDBObject object = new MovieDBObject(obj.getDb_id(), obj.getTitle(), obj.getOverview(),
                                    obj.getReleaseDate(), obj.getRating(), poster, backdrop, reviews, trailers);

                            Log.i("database", obj.getDb_id() + obj.getTitle() + poster + backdrop);
                            movieDBHelper.insertMovie(object);
                        }
                    };
                    MainActivity.getObjectDetails(obj.getDb_id(), callback);
                }
                else { //Movie already present in DB
                    view.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.favorite_blank));
                    movieDBHelper.deleteMovie(obj.getDb_id());
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    pageCount += 1;
                    prepareList(pageCount);

                    loading = true;
                }
            }
        });

        return rootView;
    }

    private void prepareList(int pageNo){
        //Add theMovieDB API token in gradle.properties    MyTheMovieDBApiToken="xxxxxxxxxxxxxxxxxxxxxx"
        String URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN + "&page=" + pageNo;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            String title = object.getString("original_title");
            String db_id = object.getString("id");
            String poster_url = "http://image.tmdb.org/t/p/w342" + object.getString("poster_path");
            String backdrop_url = "http://image.tmdb.org/t/p/w780" + object.getString("backdrop_path");
            String rating = object.getString("vote_average");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");

            MovieClass movie = new MovieClass(title, db_id, poster_url);
            movie.setBackdropURL(backdrop_url);
            movie.setOverview(overview);
            movie.setRating(rating);
            movie.setReleaseDate(release_date);

            list.add(movie);
        }
        adapter.notifyDataSetChanged();
    }
}
