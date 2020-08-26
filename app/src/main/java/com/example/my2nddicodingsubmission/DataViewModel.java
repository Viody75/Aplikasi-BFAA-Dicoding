package com.example.my2nddicodingsubmission;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DataViewModel extends ViewModel {

    final static private String TAG = DataViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<UserModel>> listUsers = new MutableLiveData<>();

    void setUserData(final String query){
        Log.d(TAG, "setUserData : running ................. ");
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    LiveData<ArrayList<UserModel>> getUsers() {
        return listUsers;
    }

}
