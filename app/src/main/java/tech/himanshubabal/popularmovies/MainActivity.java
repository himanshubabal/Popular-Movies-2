package tech.himanshubabal.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: 7/5/16 -> pull to refresh
// TODO: 7/5/16 -> cache management
public class MainActivity extends AppCompatActivity {
    //private AHBottomNavigation bottomNavigation;
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    RequestQueue requestQueue;
    MovieDBHelper dbHelper;
    public static Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        MainActivity.context = getApplicationContext();

        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(context)
        );
        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
        requestQueue = Volley.newRequestQueue(this);
        setProxy();

        dbHelper = new MovieDBHelper(this);
        //dbHelper.onUpdate();

        //bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem popularMovies = new AHBottomNavigationItem("Popular Movies",
                                                R.drawable.ic_theaters_white_18dp, R.color.colorPopular);
        AHBottomNavigationItem topMovies = new AHBottomNavigationItem("Top Rated Movies",
                                                R.drawable.ic_star_rate_white_18dp, R.color.colorTop);
        AHBottomNavigationItem myMovies = new AHBottomNavigationItem("My Movies",
                                                R.drawable.ic_bookmark_white_18dp, R.color.colorMy);

        bottomNavigation.addItem(popularMovies);
        bottomNavigation.addItem(topMovies);
        bottomNavigation.addItem(myMovies);

        bottomNavigation.setBehaviorTranslationEnabled(false);

        //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        //bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        //bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        //bottomNavigation.setForceTint(true);

        //bottomNavigation.setForceTitlesDisplay(true);

        bottomNavigation.setColored(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        //bottomNavigation.setNotification("4", 1);
        //bottomNavigation.setNotification("", 1);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 0) {
                    PopularMovies popularMovies1 = new PopularMovies();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_for_fragment, popularMovies1);
                    transaction.commit();
                }
                if (position == 1) {
                    TopMovies topMovies1 = new TopMovies();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_for_fragment, topMovies1);
                    transaction.commit();
                }
                if (position == 2) {
                    FavouriteMovies favouriteMovies = new FavouriteMovies();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_for_fragment, favouriteMovies);
                    transaction.commit();
                }
                return true;
            }
        });
    }

    private void setProxy() {
        //Device proxy settings
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        Proxy proxy = null;
        List<Proxy> proxyList = defaultProxySelector.select(URI.create("http://www.google.in"));
        if (proxyList.size() > 0) {
            proxy = proxyList.get(0);

            Log.d("proxy", String.valueOf(proxy));

            try {
                String proxyType = String.valueOf(proxy.type());

                //setting HTTP Proxy
                if (proxyType.equals("HTTP")) {
                    String proxyAddress = String.valueOf(proxy.address());
                    String[] proxyDetails = proxyAddress.split(":");
                    String proxyHost = proxyDetails[0];
                    String proxyPort = proxyDetails[1];
                    Log.d("proxy", proxyType + " " + proxyHost + " " + proxyPort);

                    System.setProperty("http.proxyHost", proxyHost);
                    System.setProperty("http.proxyPort", proxyPort);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void getObjectDetails (String db_id, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int _id = Integer.parseInt(db_id);
        String URL_A = "http://api.themoviedb.org/3/movie/" + _id + "/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN ;
        String URL_B = "http://api.themoviedb.org/3/movie/" + _id + "/videos?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN ;

        StringRequest stringRequest_A = new StringRequest(Request.Method.GET, URL_A,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSON_A(response, callback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest_A);

        StringRequest stringRequest_B = new StringRequest(Request.Method.GET, URL_B,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSON_B(response, callback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest_B);
    }

    private static void parseJSON_A (String response, VolleyCallback callback) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        String[] res = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            String author = object.getString("author");
            String content = object.getString("content");

            String review = DBUtility.convArrToStr(new String[]{author, content});
            res[i] = review;
        }

        callback.onSuccessArrayResp_A(res);
    }

    private static void parseJSON_B (String response, VolleyCallback callback) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        String[] res = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            String videoLink = object.getString("key");
            String name = object.getString("name");

            String review = DBUtility.convArrToStr(new String[]{videoLink, name});
            res[i] = review;
        }

        callback.onSuccessArrayResp_B(res);
    }

}
