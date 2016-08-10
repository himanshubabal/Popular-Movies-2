package tech.himanshubabal.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class FragmentYoutubeVideo extends Fragment implements YouTubePlayer.OnInitializedListener {

    public static final String DEVELOPER_KEY = BuildConfig.YOUTUBE_API;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private String url;

    YouTubePlayerSupportFragment youTubePlayerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_container, container, false);

        Bundle bundle = getArguments();
        url = bundle.getString("key");

        youTubePlayerView = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_video_fragment, youTubePlayerView).commit();

        youTubePlayerView.initialize(DEVELOPER_KEY, this);

        return view;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            youTubePlayer.cueVideo(url);

            // Player Style
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "Can not play video";
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}