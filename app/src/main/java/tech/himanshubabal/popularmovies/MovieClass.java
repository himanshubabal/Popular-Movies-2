package tech.himanshubabal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieClass implements Parcelable {
    private String title;
    private String db_id;
    private String posterUrl;
    private String overview;
    private String releaseDate;
    private String rating;
    private String backdropURL;

    private String[] trailers;
    private String[] reviews;



    public String[] getTrailers() {
        return trailers;
    }

    public void setTrailers(String[] trailers) {
        this.trailers = trailers;
    }

    public String[] getReviews() {
        return reviews;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public String getBackdropURL() {
        return backdropURL;
    }

    public void setBackdropURL(String backdropURL) {
        this.backdropURL = backdropURL;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public MovieClass (String title, String db_id, String posterUrl){
        this.title = title;
        this.db_id = db_id;
        this.posterUrl = posterUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.db_id);
        dest.writeString(this.posterUrl);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.rating);
        dest.writeString(this.backdropURL);
        dest.writeStringArray(this.trailers);
        dest.writeStringArray(this.reviews);
    }

    protected MovieClass(Parcel in) {
        this.title = in.readString();
        this.db_id = in.readString();
        this.posterUrl = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readString();
        this.backdropURL = in.readString();
        this.trailers = in.createStringArray();
        this.reviews = in.createStringArray();
    }

    public static final Parcelable.Creator<MovieClass> CREATOR = new Parcelable.Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel source) {
            return new MovieClass(source);
        }

        @Override
        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };
}
