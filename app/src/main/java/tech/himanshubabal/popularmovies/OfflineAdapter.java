package tech.himanshubabal.popularmovies;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.OfflineHolder>{
    private Context context;
    private List<MovieDBObject> list;
    OnItemClickListener onItemClickListener;

    public class OfflineHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_grid_imageView)
        ImageView imageView;
        @BindView(R.id.fav_icon_grid_photo)
        ImageView favIconImageView;

        public OfflineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            favIconImageView.setOnClickListener(this);
            favIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favourite_filled));
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

    public OfflineAdapter (Context context, List<MovieDBObject> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick (View view, int position);
        void onIconClick (ImageView view, int position);
    }

    public void setOnItemClickListener (final OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public OfflineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_imageview, parent, false);
        return new OfflineHolder(view);
    }

    @Override
    public void onBindViewHolder(OfflineHolder holder, int position) {
        MovieDBObject object = list.get(position);
        holder.imageView.setImageBitmap(object.getPoster());
    }

    public void updateList(List<MovieDBObject> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        Log.i("bitmap", "list-size : " + String.valueOf(list.size()));
        return list.size();
    }
}
