package tech.himanshubabal.popularmovies;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity{
    @BindView(R.id.backdrop_image)
    ImageView backdropImage;
    @BindView(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_detail_view_pager)
    ViewPager viewPager;
    @BindView(R.id.movie_detail_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movie_detail_fragment_container)
    LinearLayout linearLayoutContainer;
    MovieDBHelper movieDBHelper;
    public Bitmap posterImageBitmap, backdropImageBitmap;
    public String title, overview, rating, releaseDate;
    public int db_id;
    public static String[] reviews, trailers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));


        movieDBHelper = new MovieDBHelper(this);

        if (getIntent().getExtras().containsKey("fragment")) {
            if (getIntent().getExtras().getString("fragment").equals("db")){
                String db_ID = getIntent().getExtras().getString("object");
                MovieDBObject object = movieDBHelper.findMovieRow(db_ID);
                db_id = Integer.parseInt(object.getDb_id());
                posterImageBitmap = object.getPoster();
                backdropImageBitmap = object.getBackdrop();
                title = object.getTitle();
                overview = object.getOverview();
                rating = object.getRating();
                releaseDate = object.getRelease_date();

                String[] trailers = object.getTrailers();
                String[] reviews = object.getReviews();
                this.reviews = reviews;
                this.trailers = trailers;
                Log.i("debug-movie", String.valueOf(Arrays.asList(trailers)));
                Log.i("debug-movie", String.valueOf(Arrays.asList(reviews)));

            }
            else { //Data to be retrieved from database
                MovieClass object = getIntent().getExtras().getParcelable("object");

                int posterPixelWidth = DBUtility.getPosterPixels(getApplicationContext())[0];
                int posterPixelHeight = DBUtility.getPosterPixels(getApplicationContext())[1];
                int backdropPixelWidth = DBUtility.getBackdropPixels(getApplicationContext())[0];
                int backdropPixelHeight = DBUtility.getBackdropPixels(getApplicationContext())[1];

                posterImageBitmap = DBUtility.getResizedBitmap(DBUtility.getBitmapFromURL(object.getPosterUrl()), posterPixelWidth, posterPixelHeight);
                backdropImageBitmap = DBUtility.getResizedBitmap(DBUtility.getBitmapFromURL(object.getBackdropURL()), backdropPixelWidth, backdropPixelHeight);
                title = object.getTitle();
                overview = object.getOverview();
                rating = object.getRating();
                releaseDate = object.getReleaseDate();
                db_id = Integer.parseInt(object.getDb_id());

                String[] trailers = object.getTrailers();
                String[] reviews = object.getReviews();
                this.reviews = reviews;
                this.trailers = trailers;

                Log.i("debug-movie", String.valueOf(Arrays.asList(trailers)));
                Log.i("debug-movie", String.valueOf(Arrays.asList(reviews)));
            }
        }

        backdropImage.setImageBitmap(backdropImageBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentDetails(), "Details");
        adapter.addFragment(new FragmentReviews(), "Reviews");
        adapter.addFragment(new FragmentTrailers(), "Trailers");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
