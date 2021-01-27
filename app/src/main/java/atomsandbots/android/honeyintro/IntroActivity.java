package atomsandbots.android.honeyintro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import atomsandbots.android.honeyintro.databinding.ActivityIntroBinding;


public class IntroActivity extends AppCompatActivity {


    IntroViewPagerAdapter introViewPagerAdapter;
    int position = 0;
    Animation btnAnim;

    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // check if the user has already opened welcome screen before launching
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
        setContentView(v);
        // hide the action bar
        getSupportActionBar().hide();

        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);


        // fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Fresh Food", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img1));
        mList.add(new ScreenItem("Fast Delivery", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img2));
        mList.add(new ScreenItem("Easy Payment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img3));

        // setup viewpager
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        binding.screenViewpager.setAdapter(introViewPagerAdapter);

        // setup tabLayout with viewpager
        binding.tabIndicator.setupWithViewPager(binding.screenViewpager);

        //Net button to get next item on screen pager
        binding.btnNext.setOnClickListener(v1 -> {
            position = binding.screenViewpager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                binding.screenViewpager.setCurrentItem(position);
            }

            if (position == mList.size() - 1) { // when we reach to the last screen
                onLastScreen();
            }

        });

        // tabLayout add change listener
        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    onLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Get Started button click listener
        binding.btnGetStarted.setOnClickListener(v12 -> {
            //open main activity
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // we are using shared preferences to that process
            savePrefsData();
            finish();

        });

        // skip button click listener
        binding.tvSkip.setOnClickListener(v13 -> binding.screenViewpager.setCurrentItem(mList.size()));

    }

    private boolean restorePrefData() {
        //check to see is user has opened app before
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened", false);
    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

    // show the 'get started' Button and hide the indicator and the next button
    private void onLastScreen() {

        binding.btnNext.setVisibility(View.INVISIBLE);
        binding.btnGetStarted.setVisibility(View.VISIBLE);
        binding.tvSkip.setVisibility(View.INVISIBLE);
        binding.tabIndicator.setVisibility(View.INVISIBLE);
        // setup animation
        binding.btnGetStarted.setAnimation(btnAnim);
    }
}