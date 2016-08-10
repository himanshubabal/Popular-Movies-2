package tech.himanshubabal.popularmovies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class MovieDBObject implements Parcelable{
    private String db_id, title, overview, release_date, rating;
    private Bitmap poster, backdrop;
    private String[] reviews, trailers;
    private long _id;

    public MovieDBObject(Parcel in) {
        db_id = in.readString();
        title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        rating = in.readString();
        poster = in.readParcelable(Bitmap.class.getClassLoader());
        backdrop = in.readParcelable(Bitmap.class.getClassLoader());
        reviews = in.createStringArray();
        trailers = in.createStringArray();
        _id = in.readLong();
    }

    public static final Creator<MovieDBObject> CREATOR = new Creator<MovieDBObject>() {
        @Override
        public MovieDBObject createFromParcel(Parcel in) {
            return new MovieDBObject(in);
        }

        @Override
        public MovieDBObject[] newArray(int size) {
            return new MovieDBObject[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public MovieDBObject (String db_id, String title, String overview, String release_date,
                          String rating, Bitmap poster, Bitmap backdrop, String[] reviews, String[] trailers ) {
        this.db_id = db_id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.rating = rating;
        this.poster = poster;
        this.backdrop = backdrop;
        this.reviews = reviews;
        this.trailers = trailers;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public Bitmap getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(Bitmap backdrop) {
        this.backdrop = backdrop;
    }

    public String[] getReviews() {
        return reviews;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public String[] getTrailers() {
        return trailers;
    }

    public void setTrailers(String[] trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(db_id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(rating);
        parcel.writeParcelable(poster, i);
        parcel.writeParcelable(backdrop, i);
        parcel.writeStringArray(reviews);
        parcel.writeStringArray(trailers);
        parcel.writeLong(_id);
    }
}
