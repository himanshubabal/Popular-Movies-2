package tech.himanshubabal.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentReviews extends Fragment{
    @BindView(R.id.detail_frag_reviews_recyclerView)
    RecyclerView recyclerView;

    FragmentManager fragmentManager;
    List<FragmentReviewAdapter.ReviewObject> reviewObjects = new ArrayList<>();
    private int db_id;
    private RequestQueue requestQueue;
    FragmentReviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_reviews, container, false);

        db_id = ((MovieDetails)getActivity()).db_id;

        ButterKnife.bind(this, view);
        fragmentManager = getChildFragmentManager();
        requestQueue = Volley.newRequestQueue(getContext());

        adapter = new FragmentReviewAdapter(reviewObjects);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getList();
        return view;
    }

    private void getList() {
        String url = "https://api.themoviedb.org/3/movie/" + db_id + "/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("results");

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String author = obj.getString("author");
            String content = obj.getString("content");

            reviewObjects.add(new FragmentReviewAdapter.ReviewObject(author, content));
        }
        adapter.notifyDataSetChanged();
    }
}
