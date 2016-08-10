package tech.himanshubabal.popularmovies;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DBUtility {
    public static String strSeparator = "__,__";
    public static String separator = "==,==";

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public static String convertArrayToString(String[] array){
        if (array != null) {
            String str = "";
            for (int i = 0; i < array.length; i++) {
                str = str + array[i];
                // Do not append comma at the end of last element
                if (i < array.length - 1) {
                    str = str + strSeparator;
                }
            }
            return str;
        }
        else {
            return null;
        }
    }
    public static String[] convertStringToArray(String str){
        if (str != null) {
            String[] arr = str.split(strSeparator);
            return arr;
        }
        else {
            return null;
        }
    }


    public static String convArrToStr(String[] array){
        if (array != null) {
            String str = "";
            for (int i = 0; i < array.length; i++) {
                str = str + array[i];
                // Do not append comma at the end of last element
                if (i < array.length - 1) {
                    str = str + separator;
                }
            }
            return str;
        }
        else {
            return null;
        }
    }
    public static String[] convStrToArr(String str){
        if (str != null) {
            String[] arr = str.split(separator);
            return arr;
        }
        else {
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Context context, String name){
        name = name + ".jpg";
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);


        Log.i("bitmap", mypath.getAbsolutePath());
        //Log.i("bitmap", mypath.getPath());
        //Log.i("bitmap", mypath.getName());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("bitmap", directory.getAbsolutePath());
        //Log.i("bitmap", directory.getPath());
        //Log.i("bitmap", directory.getName());
        return mypath.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(String path) {
        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromURL (String link) {
        try {
            Bitmap bitmap = new bitmapDownload().execute(link).get();
            return bitmap;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static int[] getBackdropPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        final float scale = context.getResources().getDisplayMetrics().density;

        float backDropRatio = (float)780/(float)439;
        int backdropWidthDP = (int) (dpWidth);
        int backdropHeightDP = (int) (dpWidth / backDropRatio);
        int backdropWidthPixel = (int) (backdropWidthDP * scale + 0.5f);
        int backdropHeightPixel = (int) (backdropHeightDP * scale + 0.5f);

        return new int[]{backdropWidthPixel, backdropHeightPixel};
    }

    public static int[] getPosterPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        float imageRatio = (float)342/(float)513;
        final float scale = context.getResources().getDisplayMetrics().density;
        int widthDp = (int) (dpWidth) / 2;
        int heightDp = (int) (widthDp / imageRatio);
        int widthPixels = (int) (widthDp * scale + 0.5f);
        int heightPixels = (int) (heightDp * scale + 0.5f);

        return new int[]{widthPixels, heightPixels};
    }

    private static class bitmapDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL myurl = new URL(strings[0]);
                Bitmap bmp = BitmapFactory.decodeStream(myurl.openConnection().getInputStream());
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }


}
