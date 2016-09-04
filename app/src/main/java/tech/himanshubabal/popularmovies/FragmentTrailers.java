package tech.himanshubabal.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

public class FragmentTrailers extends Fragment {
//    public static final String DEVELOPER_KEY = BuildConfig.YOUTUBE_API;
//    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private String url;
    @BindView(R.id.detail_frag_trailer_recyclerView)
    RecyclerView recyclerView;
    //FragmentTransaction transaction;
    FragmentManager fragmentManager;
    List<FragmentTrailerAdapter.TrailerObject> trailerObjectList = new ArrayList<>();

//    YouTubePlayerSupportFragment youTubePlayerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_trailers, container, false);

        String[] tr = MovieDetails.trailers;
        url = DBUtility.convStrToArr(tr[0])[0];
        Log.i("movie-debug", url);

        for (int i = 0; i < tr.length; i++) {
            String a = DBUtility.convStrToArr(tr[i])[0];
            String b = DBUtility.convStrToArr(tr[i])[1];
            FragmentTrailerAdapter.TrailerObject o = new FragmentTrailerAdapter.TrailerObject(a, b);
            trailerObjectList.add(o);
        }

        ButterKnife.bind(this, view);
        //transaction = getChildFragmentManager();.beginTransaction();
        fragmentManager = getChildFragmentManager();

        FragmentTrailerAdapter adapter = new FragmentTrailerAdapter(trailerObjectList, getContext(), fragmentManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(new FragmentTrailerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String u = trailerObjectList.get(position).getYoutubeID();
//                Bundle bundle = new Bundle();
//                bundle.putString("key", u);
//                FragmentYoutubeVideo fragmentYoutubeVideo = new FragmentYoutubeVideo();
//                fragmentYoutubeVideo.setArguments(bundle);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.youtube_container, fragmentYoutubeVideo);
//                transaction.commit();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + u)));
            }

            @Override
            public void onIconClick(ImageView view, int position) {

            }
        });

//        youTubePlayerView = (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
//        youTubePlayerView = YouTubePlayerSupportFragment.newInstance();

//        transaction.add(R.id.youtube_fragment, youTubePlayerView).commit();
//
//
//        Log.i("movie-debug", DEVELOPER_KEY);
//        youTubePlayerView.initialize(DEVELOPER_KEY, this);


        return view;
    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        if (!b) {
//
//            // loadVideo() will auto play video
//            // Use cueVideo() method, if you don't want to play it automatically
//            youTubePlayer.cueVideo(url);
//
//            // Player Style
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//        }
//    }

//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            String errorMessage = "Can not play video";
//            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
//        }
//    }
//
//    private YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return youTubePlayerView;
//    }
}
