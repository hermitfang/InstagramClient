package com.hermitfang.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "966c023997bd4b38a7e0fce453fe1965";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter adapterPhotos;
    // private SwipeRefreshLayout srSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // search "Android async http" -- the library helps to get api via http in async way
            // compile 'com.loopj.android:android-async-http:1.4.8'
        photos = new ArrayList<>();
        adapterPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(adapterPhotos);

        fetchPopularPhotos();

        // search "Picasso" -- the library helps to get images from specified url
            // compile 'com.squareup.picasso:picasso:2.5.2'
    }

    public void fetchPopularPhotos() {
        /*
        client id: 966c023997bd4b38a7e0fce453fe1965
        https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        response:
        data[] . type == 'image' (must be image)
        data[] . caption . text == {caption
        data[] . user . username == {user name}
        data[] . user . full_name == {user's full name}
        data[] . images . standard_resolution . url {image url}
        data[] . images . standard_resolution . width {image width}
        data[] . images . standard_resolution . height {image height}
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // super.onSuccess(statusCode, headers, response);
                Log.i("DEBUG", response.toString());

                JSONArray photosJ = null;
                try {
                    photosJ = response.getJSONArray("data");
                    for(int i=0; i<photosJ.length(); i++) {
                        JSONObject photoJ = photosJ.getJSONObject(i);
                        InstagramPhoto p = new InstagramPhoto();

                        p.imageUrl = photoJ.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        p.caption = photoJ.getJSONObject("caption").getString("text") + "\n\n";
                        p.username = photoJ.getJSONObject("user").getString("username");
                        p.profilePicture = photoJ.getJSONObject("user").getString("profile_picture");
                        p.likes = "Likes: " + photoJ.getJSONObject("likes").getString("count");
                        p.timeStamp = photoJ.getString("created_time");
                        p.username = p.username + " Created Time: " + p.timeStamp;

                        JSONArray arr = photoJ.getJSONObject("comments").getJSONArray("data");
                        if (arr.length() > 0) {
                            String fromUser = arr.getJSONObject(arr.length() - 1).getJSONObject("from").getString("username");
                            p.comments = "\t" + fromUser + " said: " + arr.getJSONObject(arr.length() - 1).getString("text");
                            if (arr.length() > 1) {
                                fromUser = arr.getJSONObject(arr.length() - 2).getJSONObject("from").getString("username");
                                p.comments = p.comments + "\n\n\t" + fromUser + " said: " + arr.getJSONObject(arr.length() - 2).getString("text");
                            }
                        }
                        else {
                            p.comments = "";
                        }

                        photos.add(p);
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }

                adapterPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.i("DEBUG", errorResponse.toString());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
