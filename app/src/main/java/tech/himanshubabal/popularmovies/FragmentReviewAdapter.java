package tech.himanshubabal.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FragmentReviewAdapter extends RecyclerView.Adapter<FragmentReviewAdapter.ReviewViewHolder>{
    public static final String DEVELOPER_KEY = BuildConfig.YOUTUBE_API;
    private List<ReviewObject> list;

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviererName;
        TextView review;

        public ReviewViewHolder (View itemView) {
            super(itemView);
            reviererName = (TextView) itemView.findViewById(R.id.reviewer_name_textView);
            review = (TextView) itemView.findViewById(R.id.review_textView);
        }
    }

    public FragmentReviewAdapter(List<ReviewObject> list) {
        this.list = list;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_recyclerview, parent, false);

        return new ReviewViewHolder (view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewObject object = list.get(position);
        holder.review.setText(object.getReview());
        holder.reviererName.setText(object.getReviewerName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReviewObject {
        private String reviewerName;
        private String review;

        public ReviewObject(String reviewerName, String review) {
            this.reviewerName = reviewerName;
            this.review = review;
        }

        public String getReviewerName() {
            return reviewerName;
        }

        public void setReviewerName(String reviewerName) {
            this.reviewerName = reviewerName;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }
}

