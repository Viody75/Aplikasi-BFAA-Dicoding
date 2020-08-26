package com.example.my2nddicodingsubmission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DetailUserActivity extends AppCompatActivity {

    TextView tvUsername, tvName, tvEmail;

    String dataUsername;

    private final static String TAG = DetailUserActivity.class.getSimpleName();

    ImageView ivUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        setTitle("User Detail");

        tvUsername = findViewById(R.id.tv_username);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);

        ivUser = findViewById(R.id.img_item_photo);

        dataUsername = getIntent().getStringExtra("username");

        tvUsername.setText(dataUsername);

        getSetData(dataUsername);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(),this, dataUsername );
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
    }

    private void getSetData(String query) {

        Log.d(TAG, "getData : running");
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = " https://api.github.com/users/" + query;
        client.addHeader("Authorization","token cb01296d9a2a165b3cfd40ac4fc8db9d4966180e");
        client.addHeader("User-Agent", "Request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String name = responseObject.getString("name");
                    String username = responseObject.getString("login");
                    String location = responseObject.getString("location");
                    String avatar = responseObject.getString("avatar_url");

                    tvUsername.setText(name);
                    tvName.setText(username);
                    tvEmail.setText(location);

                    Glide.with(DetailUserActivity.this)
                            .load(avatar)
                            .apply(new RequestOptions().override(150, 150))
                            .into(ivUser);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
