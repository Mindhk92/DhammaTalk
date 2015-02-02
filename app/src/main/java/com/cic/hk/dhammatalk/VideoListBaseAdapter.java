package com.cic.hk.dhammatalk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 1/15/2015.
 */
public class VideoListBaseAdapter extends BaseAdapter {

    private static ArrayList<ItemDetailVideoList> list;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private LayoutInflater l_Inflater;
    private final ThumbnailListener thumbnailListener;

    public VideoListBaseAdapter(Context context, ArrayList<ItemDetailVideoList> result){
        list = result;
        this.l_Inflater = LayoutInflater.from(context);
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
//
//                .build();
//        ImageLoader.getInstance().init(config);

        thumbnailViewToLoaderMap = new HashMap<>();
        thumbnailListener = new ThumbnailListener();
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
         String video_url = list.get(position).getVideo_url();
        if (convertView == null){
            convertView = l_Inflater.inflate(R.layout.itemdetail_videolist, null);
            holder = new ViewHolder();
            holder.image_url = (YouTubeThumbnailView) convertView.findViewById(R.id.gambar_video);
//            YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.image_url);
            holder.image_url.setTag(video_url);
            holder.image_url.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            holder.title_url = (TextView) convertView.findViewById(R.id.title_video);
            holder.video_url = (TextView) convertView.findViewById(R.id.url_video);
            holder.upload_date = (TextView) convertView.findViewById(R.id.upload_date);
            holder.view_count = (TextView) convertView.findViewById(R.id.view_count);
            convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }

        YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.image_url);
        if (loader == null) {
            // 2) The view is already created, and is currently being initialized. We store the
            //    current videoId in the tag.
            holder.image_url.setTag(video_url);
        } else {
            // 3) The view is already created and already initialized. Simply set the right videoId
            //    on the loader.
            holder.image_url.setImageResource(R.drawable.loading);
            loader.setVideo(video_url);
        }
        //ImageLoader.getInstance().displayImage(list.get(position).getImage_url(), holder.image_url);
        holder.title_url.setText(list.get(position).getTitle());
        holder.video_url.setText(list.get(position).getVideo_url());

        //HttpHost targetHost = new HttpHost("113.212.160.12");
        //HttpGet targetGet = new HttpGet("/wihara/getListVideo.php");
        //Log.d("urlku", base_urlku+ (btnNewest.isEnabled()?"getListVideoByPopular.php":"getListVideoByNewest.php"));
        // Log.d(TAG, "Hello!");
       new RetrieveYoutubeVideoDetail(list.get(position).getVideo_url(),holder.view_count, holder.upload_date).execute();

        return convertView;
    }

    static class ViewHolder{
        YouTubeThumbnailView image_url;
        TextView title_url;
        TextView video_url;
        TextView upload_date;
        TextView view_count;
    }
    class RetrieveYoutubeVideoDetail extends AsyncTask<String, Void, String> {

//        private Exception exception;

        private String videoID= "";
        private TextView viewCount;
        private TextView publishedDate;

        public RetrieveYoutubeVideoDetail(String v, TextView tv1, TextView tv2){
            this.videoID = v;
            this.viewCount = tv1;
            this.publishedDate = tv2;
        }

        protected String doInBackground(String... urls) {
            String txtResult = "";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String youtubeJSON = "http://gdata.youtube.com/feeds/api/videos/"+ this.videoID +"?v=1&alt=json";
            Log.d("youtubeJSON", youtubeJSON);
            HttpGet httppost = new HttpGet(youtubeJSON);
            try {
                HttpResponse response = httpClient.execute(httppost);
                //response = httpClient.execute(targetHost, targetGet);
                HttpEntity entity = response.getEntity();
                txtResult = EntityUtils.toString(entity);
                //  Log.d(TAG, ""+response);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.e("ClientProtocolException", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("IOException", e.getMessage());
                e.printStackTrace();
            }
            return txtResult;
        }

        protected void onPostExecute(String htmlResponse) {

            Log.d("onPostExecute(htmlResponse)", htmlResponse);
            try {

                JSONObject mainJsonObject = new JSONObject(htmlResponse);
                JSONObject entryJsonObject = mainJsonObject.getJSONObject("entry");

                JSONObject publishedJsonObject = entryJsonObject.getJSONObject("published");
                String publishedDate = publishedJsonObject.getString("$t").substring(0,10);
                Log.d("publishedDate", publishedDate);
                SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                    Date newDate = format.parse(publishedDate);

                    format = new SimpleDateFormat("yyyy, MMM dd");
                    String date = format.format(newDate);

                    this.publishedDate.setText(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONObject ytstatisticsJsonObject = entryJsonObject.getJSONObject("yt$statistics");
                long viewcountJsonObject = ytstatisticsJsonObject.getLong("viewCount");

                this.viewCount.setText(viewcountJsonObject+"");
                //  Log.d(TAG, ""+response);

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);
            view.setImageResource(R.drawable.loading);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.teratai);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.loading);
        }
    }

}
