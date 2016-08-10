package tech.himanshubabal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteMovies extends Fragment{
    @BindView(R.id.favourite_movies_recyclerView)
    RecyclerView recyclerView;
    List<MovieDBObject> list = new ArrayList<>();
    MovieDBHelper movieDBHelper;
    OfflineAdapter offlineAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav_movies, container, false);
        ButterKnife.bind(this, rootView);

        movieDBHelper = ((MainActivity)getActivity()).dbHelper;
        list = movieDBHelper.getAllMovies();

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        offlineAdapter = new OfflineAdapter(getContext(), list);
        prepareList();
        recyclerView.setAdapter(offlineAdapter);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        offlineAdapter.setOnItemClickListener(new OfflineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MovieDBObject object = list.get(position);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra("fragment", "db");
                intent.putExtra("object", object.getDb_id());
                startActivity(intent);
            }

            @Override
            public void onIconClick(ImageView view, final int position) {
                final MovieDBObject object = list.get(position);
                movieDBHelper.deleteMovie(object.getDb_id());
                list.remove(position);
                offlineAdapter.updateList(list);
                offlineAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    private void prepareList(){
        Log.i("bitmap", list.size()+"");
        offlineAdapter.updateList(list);
        offlineAdapter.notifyDataSetChanged();
    }
}
