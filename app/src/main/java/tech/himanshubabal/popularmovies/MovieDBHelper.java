package tech.himanshubabal.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MovieDBHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "user_movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = " , ";
    private static final String NOT_NULL = " NOT NULL";
    private static final String UNIQUE = " UNIQUE";

    private static final String CREATE_ENTRIES =
            "CREATE TABLE " + DBEntry.TABLE_NAME + " (" +
                    DBEntry._ID + " INTEGER PRIMARY KEY," +
                    DBEntry.COLUMN_NAME_DB_ID + TYPE_TEXT + NOT_NULL + UNIQUE + COMMA_SEP +
                    DBEntry.COLUMN_NAME_TITLE + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_OVERVIEW + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_RELEASE_DATE + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_RATING + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_POSTER_PATH + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_BACKDROP_PATH + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                    DBEntry.COLUMN_NAME_REVIEWS + TYPE_TEXT + COMMA_SEP +
                    DBEntry.COLUMN_NAME_TRAILERS + TYPE_TEXT +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void onUpdate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public boolean insertMovie (String db_id, String title, String overview, String release_date,
                                String rating, Bitmap poster, Bitmap backdrop, String[] reviews, String[] trailers) {
        //byte[] poster_image = DBUtility.getBytes(poster);
        //byte[] backdrop_image = DBUtility.getBytes(backdrop);
        String poster_path = DBUtility.saveToInternalStorage(poster, context, db_id + "poster");
        String backdrop_path = DBUtility.saveToInternalStorage(backdrop, context, db_id + "backdrop");

        String reviews_string = DBUtility.convertArrayToString(reviews);
        String trailer_string = DBUtility.convertArrayToString(trailers);

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBEntry.COLUMN_NAME_DB_ID, db_id);
        contentValues.put(DBEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(DBEntry.COLUMN_NAME_OVERVIEW, overview);
        contentValues.put(DBEntry.COLUMN_NAME_RELEASE_DATE, release_date);
        contentValues.put(DBEntry.COLUMN_NAME_RATING, rating);
        contentValues.put(DBEntry.COLUMN_NAME_POSTER_PATH, poster_path);
        contentValues.put(DBEntry.COLUMN_NAME_BACKDROP_PATH, backdrop_path);
        contentValues.put(DBEntry.COLUMN_NAME_REVIEWS, reviews_string);
        contentValues.put(DBEntry.COLUMN_NAME_TRAILERS, trailer_string);

        long newRowID;
        newRowID = database.insert(DBEntry.TABLE_NAME, null, contentValues);
        database.close();
        return ( newRowID != -1 );
    }

    public boolean insertMovie (MovieDBObject object) {
        //byte[] poster_image = DBUtility.getBytes(object.getPoster());
        //byte[] backdrop_image = DBUtility.getBytes(object.getBackdrop());
        String poster_path = DBUtility.saveToInternalStorage(object.getPoster(), context, object.getDb_id() + "poster");
        String backdrop_path = DBUtility.saveToInternalStorage(object.getBackdrop(), context, object.getDb_id() + "backdrop");

        String reviews_string = DBUtility.convertArrayToString(object.getReviews());
        String trailer_string = DBUtility.convertArrayToString(object.getTrailers());

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBEntry.COLUMN_NAME_DB_ID, object.getDb_id());
        contentValues.put(DBEntry.COLUMN_NAME_TITLE, object.getTitle());
        contentValues.put(DBEntry.COLUMN_NAME_OVERVIEW, object.getOverview());
        contentValues.put(DBEntry.COLUMN_NAME_RELEASE_DATE, object.getRelease_date());
        contentValues.put(DBEntry.COLUMN_NAME_RATING, object.getRating());
        contentValues.put(DBEntry.COLUMN_NAME_POSTER_PATH, poster_path);
        contentValues.put(DBEntry.COLUMN_NAME_BACKDROP_PATH, backdrop_path);
        contentValues.put(DBEntry.COLUMN_NAME_REVIEWS, reviews_string);
        contentValues.put(DBEntry.COLUMN_NAME_TRAILERS, trailer_string);

        long newRowID;
        newRowID = database.insert(DBEntry.TABLE_NAME, null, contentValues);
        database.close();
        return ( newRowID != -1 );
    }

    public Boolean movieAlreadyExists(String db_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBEntry.TABLE_NAME,
                null,
                DBEntry.COLUMN_NAME_DB_ID + " = " + "'" + db_id + "'",
                null, null, null, null);

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }

        cursor.close();
        database.close();
        return hasObject;
    }

    public MovieDBObject findMovieRow (String db_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(DBEntry.TABLE_NAME,
                null,
                DBEntry.COLUMN_NAME_DB_ID + " = " + "'" + db_id + "'",
                null, null, null, null);

        cursor.moveToFirst();
        MovieDBObject object = cursorToCourseObject(cursor);
        cursor.close();
        database.close();
        return object;
    }

    public long findMovieID (String db_id) {
        MovieDBObject object = findMovieRow(db_id);
        return object.get_id();
    }

    public Boolean deleteMovie(String db_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int res = db.delete(DBEntry.TABLE_NAME, DBEntry.COLUMN_NAME_DB_ID + " = " + db_id, null);

        return (res != -1);
    }

    public List<MovieDBObject> getAllMovies () {
        List<MovieDBObject> list = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBEntry.TABLE_NAME, null,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MovieDBObject object = cursorToCourseObject(cursor);
            list.add(object);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public void printTableData(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + DBEntry.TABLE_NAME, null);
        String[] arr = cur.getColumnNames();
        String rows = "";

        for(int i = 0 ; i < arr.length; i++){
            rows = rows + " || " + arr[i];
        }

        Log.d("alldata", rows);

        if(cur.getCount() != 0){
            cur.moveToFirst();
            do{
                String row_values = "";
                for(int i = 0 ; i < cur.getColumnCount(); i++){
                    row_values = row_values + " || " + cur.getString(i);
                }
                Log.d("alldata", row_values);
            }while (cur.moveToNext());
        }
        cur.close();
        db.close();
    }

    private MovieDBObject cursorToCourseObject (Cursor cursor) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //342 x 513 --> new dimensions
        float imageRatio = (float)342/(float)513;     //img ratio is width/height
        //'scale' is used to convert DP to pixels
        final float scale = context.getResources().getDisplayMetrics().density;
        int widthDp = (int) (dpWidth) / 2;
        //  as to keep the width:height ratio same as original image
        //  height of final img = ratio * width of final img
        int heightDp = (int) (widthDp / imageRatio);
        //  final image dimensions are converted into pixels from DP
        int widthPixels = (int) (widthDp * scale + 0.5f);
        int heightPixels = (int) (heightDp * scale + 0.5f);

        float backDropRatio = (float)780/(float)439;
        int backdropWidthDP = (int) (dpWidth);
        int backdropHeightDP = (int) (dpWidth / backDropRatio);
        int backdropWidthPixel = (int) (backdropWidthDP * scale + 0.5f);
        int backdropHeightPixel = (int) (backdropHeightDP * scale + 0.5f);


        MovieDBObject object = new MovieDBObject(cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                DBUtility.getResizedBitmap(DBUtility.loadImageFromStorage(cursor.getString(6)),widthPixels, heightPixels),
                DBUtility.getResizedBitmap(DBUtility.loadImageFromStorage(cursor.getString(7)),backdropWidthPixel, backdropHeightPixel),
                DBUtility.convertStringToArray(cursor.getString(8)),
                DBUtility.convertStringToArray(cursor.getString(9)));

        object.set_id(cursor.getLong(0));

        return object;
    }

    public static abstract class DBEntry implements BaseColumns {
        private static final String TABLE_NAME = "user_movies";
        private static final String COLUMN_NAME_DB_ID = "db_id";
        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_OVERVIEW = "overView";
        private static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        private static final String COLUMN_NAME_RATING = "rating";
        private static final String COLUMN_NAME_POSTER_PATH = "poster_image";
        private static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_image";
        private static final String COLUMN_NAME_REVIEWS = "reviews";
        private static final String COLUMN_NAME_TRAILERS = "trailers";
    }
}
