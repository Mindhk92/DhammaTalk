package com.cic.hk.dhammatalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements MyTaskInterface{

    ListView myListView;
    Button btnNewest, btnPopular;

    String base_urlku = "http://113.212.160.12/wihara/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNewest = (Button) findViewById(R.id.btnNewest);
        btnPopular = (Button) findViewById(R.id.btnPopular);

        btnNewest.setEnabled(false);
        btnPopular.setEnabled(true);

        btnNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnNewest.isEnabled()){
                    //Log.d("btnNewest.setOnClickListener", ""+btnNewest.isEnabled());
                    btnNewest.setEnabled(false);
                    btnPopular.setEnabled(true);
                    //sortListByNewest();

                    new MyTask(MainActivity.this).execute();
                }
            }
        });

        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnPopular.isEnabled()){
                    //Log.d("btnPopular.setOnClickListener", ""+btnPopular.isEnabled());
                    btnNewest.setEnabled(true);
                    btnPopular.setEnabled(false);
                    //sortListByPopular();

                    new MyTask(MainActivity.this).execute();
                }
            }
        });


        new MyTask(MainActivity.this).execute();
    }

    private void sortListByNewest() {

    }
    private void sortListByPopular() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((VideoListBaseAdapter) myListView.getAdapter()).releaseLoaders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_main_action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
        if (id == R.id.menu_main_action_search) {
            return true;
        }

        if (id == R.id.action_profile) {

            Intent myIntent = new Intent (MainActivity.this, BhanteProfile.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getFromHttp(String result) {

        String aJsonString = "";

        try {

            ArrayList<ItemDetailVideoList> itemdetails = new ArrayList<ItemDetailVideoList>();
//                JSONObject jObject = new JSONObject(txtResult);
//                JSONArray jArray = jObject.getJSONArray("0");
//                aJsonString = jObject.getString("Organisation");
            JSONArray json = new JSONArray(result);
// ...

            for(int i=0;i<json.length();i++){
//                    HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(i);

                ItemDetailVideoList detail = new ItemDetailVideoList();
                detail.setID(e.getInt("id"));
                detail.setTitle(e.getString("title"));
                detail.setVideo_url(e.getString("url"));
                detail.setImage_url(e.getString("i_url"));
                itemdetails.add(detail);
            }
            initListView(itemdetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        MyTaskInterface listener;

        public MyTask(Context context){
            listener = (MyTaskInterface) context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String txtResult ="";
            //HttpHost targetHost = new HttpHost("113.212.160.12");
            //HttpGet targetGet = new HttpGet("/wihara/getListVideo.php");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(base_urlku+ (btnNewest.isEnabled()?"getListVideoByPopular.php":"getListVideoByNewest.php"));
            //Log.d("urlku", base_urlku+ (btnNewest.isEnabled()?"getListVideoByPopular.php":"getListVideoByNewest.php"));
            // Log.d(TAG, "Hello!");
            try {
                HttpResponse response = httpClient.execute(httppost);
                //response = httpClient.execute(targetHost, targetGet);
                HttpEntity entity = response.getEntity();
                String htmlResponse = EntityUtils.toString(entity);
                txtResult = htmlResponse;

                //  Log.d(TAG, ""+response);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return txtResult;
        }
        protected void onPostExecute(String result){
            if (listener != null)
            {
                listener.getFromHttp(result);
            }
        }
    }

    private void initListView(ArrayList<ItemDetailVideoList> details) {


        myListView = (ListView) findViewById(R.id.listdhammatalk);
        myListView.setAdapter(new VideoListBaseAdapter(this, details));

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                ItemDetailVideoList obj_detail = (ItemDetailVideoList) o;
//                Log.w("obj_detail.getID", "obj_detail.getID "+ obj_detail.getID());
                new UpdateViewCount(obj_detail.getID()).execute();

//                Intent myIntent = new Intent (MainActivity.this, PlayerActivity_lama.class);
                Intent myIntent = new Intent (MainActivity.this, PlayerActivity.class);
                myIntent.putExtra("filevideo", obj_detail.getVideo_url());
                myIntent.putExtra("titlevideo", obj_detail.getTitle());
                startActivity(myIntent);
            }
        });

    }

    private class UpdateViewCount extends AsyncTask<Void, Void, Void>{

        private int ID_VIDEO;

        public UpdateViewCount(int id){
            this.ID_VIDEO = id;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.w("UpdateViewCount doInBackground", "" + this.ID_VIDEO);
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://113.212.160.12/wihara/setViewCount.php");

            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("id_video", ""+this.ID_VIDEO));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {

                    String responseStr = EntityUtils.toString(resEntity).trim();

                    // you can add an if statement here and do other actions based on the response
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
