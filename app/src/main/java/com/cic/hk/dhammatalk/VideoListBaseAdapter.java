package com.cic.hk.dhammatalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
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
        this.list = result;
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

        thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
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
            YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.image_url);
            holder.image_url.setTag(video_url);
            holder.image_url.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            holder.title_url = (TextView) convertView.findViewById(R.id.title_video);
            holder.video_url = (TextView) convertView.findViewById(R.id.url_video);
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
        return convertView;
    }

    static class ViewHolder{
        YouTubeThumbnailView image_url;
        TextView title_url;
        TextView video_url;
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
