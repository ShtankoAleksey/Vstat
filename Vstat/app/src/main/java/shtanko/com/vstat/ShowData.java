package shtanko.com.vstat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ShowData extends ActionBarActivity {
//
    TextView ch_name;
    String data = "";
  //

    ImageView channel_image;

    String image_url = "http://yt3.ggpht.com/-vAC58B_yhTM/AAAAAAAAAAI/AAAAAAAAAAA/priWH4ueUBA/s88-c-k-no/photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        data = i.getStringExtra("username");

        new ParseTask().execute();

        ch_name = (TextView) findViewById(R.id.channel_name);
        //channel_image = (ImageView)findViewById(R.id.ch_image);
//        new DownloadImageTask((ImageView) findViewById(R.id.ch_image))
//                .execute(image_url);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Class for imageView
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_data, container, false);
            return rootView;
        }
    }

    //Class for name channel
    public static class DescriptionFragment extends Fragment {


        public DescriptionFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_description, container, false);
            return rootView;
        }
    }

    //Class for information
    public static class InformationFragment extends Fragment {


        public InformationFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_information, container, false);
            return rootView;
        }
    }

    //Class Json Parser
    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                String str_url = "http://gdata.youtube.com/feeds/api/users/" + data + "?alt=json";
                URL url = new URL(str_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            //Log.d(LOG_TAG, strJson);
            JSONObject dataJsonObj;
            String $t;
            String name;
            String location;
            String subscriberCount;
            String totalUploadViews;
            String photo;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONObject entryJSONObject = dataJsonObj.getJSONObject("entry");

                JSONArray authorArray = entryJSONObject.getJSONArray("author");
                JSONObject authorJSONObject = authorArray.getJSONObject(0);
                JSONObject nameJSONObject = authorJSONObject.getJSONObject("name");
                name = nameJSONObject.getString("$t");
                // Log.d(LOG_TAG, "Name: " + name);

                JSONObject contentJSONObject2 = entryJSONObject.getJSONObject("yt$location");
                location = contentJSONObject2.getString("$t");
                //Log.d(LOG_TAG, "Location: " + location);

                JSONObject contentJSONObject3 = entryJSONObject.getJSONObject("yt$statistics");
                subscriberCount = contentJSONObject3.getString("subscriberCount");
                totalUploadViews = contentJSONObject3.getString("totalUploadViews");
                // Log.d(LOG_TAG, "Subscriber Count: " + subscriberCount);
                // Log.d(LOG_TAG, "total Upload Views: " + totalUploadViews);

                JSONObject contentJSONObject = entryJSONObject.getJSONObject("content");
                $t = contentJSONObject.getString("$t");
                //Log.d(LOG_TAG, "Scheme: " + $t);

                JSONObject photoJSONObject = entryJSONObject.getJSONObject("media$thumbnail");
                photo = photoJSONObject.getString("url");

                new DownloadImageTask((ImageView) findViewById(R.id.ch_image))
                        .execute(photo);
//                tw.setText($t);
                ch_name.setText(name);
                //

//                tw3.setText(location);
//                tw4.setText(subscriberCount);
//                tw5.setText(totalUploadViews);
//                tw6.setText(photo);

/*
                number_of_subscribers.setText(subscriberCount);
                number_of_views.setText(totalUploadViews);
                //sign_up.setText();
                name_data.setText(name);
                */

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
