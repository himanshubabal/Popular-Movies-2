package tech.himanshubabal.popularmovies;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FragmentTrailerAdapter extends RecyclerView.Adapter<FragmentTrailerAdapter.TrailerViewHolder>{
    public static final String DEVELOPER_KEY = BuildConfig.YOUTUBE_API;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private List<TrailerObject> trailerObjectList;
    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private Context context;
    private FragmentManager fragmentManager;
    private OnItemClickListener onItemClickListener;

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements YouTubePlayer.OnInitializedListener ,
                                                                            View.OnClickListener{
        TextView textView;
        YouTubeThumbnailView youTubeThumbnailView;
        String url;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.youtube_trailer_name);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            itemView.setOnClickListener(this);
//            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.youtube_fragment, youTubePlayerSupportFragment);
//            transaction.commit();

        }

//        public void init (String url) {
//            this.url = url;
//            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.youtube_fragment, youTubePlayerSupportFragment);
//            transaction.commit();
//            youTubePlayerSupportFragment.initialize(DEVELOPER_KEY, this);
//        }

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
                //youTubeInitializationResult.getErrorDialog(fragment.getActivity(), RECOVERY_DIALOG_REQUEST).show();
            } else {
                String errorMessage = "Can not play video";
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View view) {
//            youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.youtube_fragment, youTubePlayerSupportFragment);
//            transaction.commit();
            //init(url);
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + url)));
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public FragmentTrailerAdapter (List<TrailerObject> trailerObjectList, Context context, FragmentManager fragmentManager) {
        this.trailerObjectList = trailerObjectList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public interface OnItemClickListener {
        void onItemClick (View view, int position);
        void onIconClick (ImageView view, int position);
    }

    public void setOnItemClickListener (final OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FragmentTrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_recyclerview, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FragmentTrailerAdapter.TrailerViewHolder holder, int position) {
        TrailerObject object = trailerObjectList.get(position);
        holder.textView.setText(object.getVideoName());
        //holder.init(object.getYoutubeID());
        Picasso.with(context).load("http://img.youtube.com/vi/"+ trailerObjectList.get(position).youtubeID +"/maxresdefault.jpg").into(holder.youTubeThumbnailView);
    }

    @Override
    public int getItemCount() {
        return trailerObjectList.size();
    }

    public static class TrailerObject {
        private String youtubeID;
        private String videoName;

        public TrailerObject (String youtubeID, String videoName) {
            this.videoName = videoName;
            this.youtubeID = youtubeID;
        }

        public String getYoutubeID() {
            return youtubeID;
        }

        public String getVideoName() {
            return videoName;
        }
    }
}
