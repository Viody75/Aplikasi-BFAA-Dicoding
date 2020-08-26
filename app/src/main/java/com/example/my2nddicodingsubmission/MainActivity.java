package com.example.my2nddicodingsubmission;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvUser;
    Button btnToDetailActivity;
    ProgressBar progressBar;
    private DataViewModel dataViewModel;
    UserAdapter userAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.search_main);

        progressBar = findViewById(R.id.progressBar);

//        getData(query);

        btnToDetailActivity = findViewById(R.id.toDetailActivity);
        btnToDetailActivity.setOnClickListener(this);


        showPB(false);

        rvUser = findViewById(R.id.rv_user);
        rvUser.setHasFixedSize(true);


        // masih coba
//        dataViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DataViewModel.class);
//        dataViewModel.getUsers().observe(this, new Observer<ArrayList<UserModel>>() {
//            @Override
//            public void onChanged(ArrayList<UserModel> userModels) {
//                if (userModels != null){
//                    userAdapter.setData(userModels);
//                    showPB(false);
//                }
//            }
//        });

    }

    private void showPB(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    showPB(true);

                    getData(query);

                    //masih coba
//                    dataViewModel.setUserData(query);
//
//                    rvUser.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                    userAdapter = new UserAdapter();
//                    userAdapter.notifyDataSetChanged();
//                    rvUser.setAdapter(userAdapter);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(String query) {

        Log.d(TAG, "getData : running" + "\n" + "\n" );
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = " https://api.github.com/search/users?q=" + query;
        client.addHeader("Authorization","token cb01296d9a2a165b3cfd40ac4fc8db9d4966180e");
        client.addHeader("User-Agent", "Request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<UserModel> listUser = new ArrayList<>();
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray items = responseObject.getJSONArray("items");
                    for (int i = 0 ; i < items.length() ; i++ ){
                    JSONObject item = items.getJSONObject(i);
                    String username = item.getString("login");
                    String avatar = item.getString("avatar_url");
                    String id = item.getString("id");

                    UserModel user = new UserModel();

                    user.setUsername(username);
                    user.setAvatar(avatar);
                    user.setId(id);

                    listUser.add(user);

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                //set adapternya
                rvUser.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                UserAdapter userAdapter = new UserAdapter(listUser,MainActivity.this);
                rvUser.setAdapter(userAdapter);
                showPB(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toDetailActivity) {
            Intent intent = new Intent(MainActivity.this, DetailUserActivity.class);
            startActivity(intent);
        }
    }
}
