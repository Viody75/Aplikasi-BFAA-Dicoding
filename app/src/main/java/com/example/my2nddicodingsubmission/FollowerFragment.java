package com.example.my2nddicodingsubmission;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerFragment extends Fragment {

    private TextView tvFollower;
    private ProgressBar progressBar;
    private RecyclerView rvUserFollower;
    private static final String TAG = FollowerFragment.class.getSimpleName();

    public FollowerFragment() {
        // Required empty public constructor
    }

    FollowerFragment newInstance(String username){
        FollowerFragment followerFragment = new FollowerFragment();
        Bundle myBundle = new Bundle();
        myBundle.putString("usernameFrag", username);
        followerFragment.setArguments(myBundle);
        return followerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        tvFollower = view.findViewById(R.id.tv_follower_fragment);
        rvUserFollower = view.findViewById(R.id.rv_follower);
        progressBar = view.findViewById(R.id.progressBar);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String username = getArguments().getString("usernameFrag");
//        tvFollower.setText(username);
        showPB(true);
        getData(username);

    }

    private void showPB(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void getData(String query) {

        Log.d(TAG, "getData : running" + "\n" + "\n" );
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = " https://api.github.com/users/" + query + "/followers";
        client.addHeader("Authorization","token cb01296d9a2a165b3cfd40ac4fc8db9d4966180e");
        client.addHeader("User-Agent", "Request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<UserModel> listUser = new ArrayList<>();
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray responseObjects = new JSONArray(result);
                    for (int i = 0 ; i < responseObjects.length() ; i++ ){
                        JSONObject item = responseObjects.getJSONObject(i);
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
                rvUserFollower.setLayoutManager(new LinearLayoutManager(getActivity()));
                UserAdapter userAdapter = new UserAdapter(listUser,getActivity());
                rvUserFollower.setAdapter(userAdapter);
                showPB(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
