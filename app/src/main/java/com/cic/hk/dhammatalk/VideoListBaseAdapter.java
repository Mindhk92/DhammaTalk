package com.cic.hk.dhammatalk;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by user on 1/15/2015.
 */
public class VideoListBaseAdapter extends BaseAdapter {

    private static ArrayList<ItemDetailVideoList> list;
    private LayoutInflater l_Inflater;

    public VideoListBaseAdapter(Context context, ArrayList<ItemDetailVideoList> result){
        this.list = result;
        this.l_Inflater = LayoutInflater.from(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app

                .build();
        ImageLoader.getInstance().init(config);
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
        if (convertView == null){
            convertView = l_Inflater.inflate(R.layout.itemdetail_videolist, null);
            holder = new ViewHolder();
            holder.image_url = (ImageView) convertView.findViewById(R.id.gambar_video);
            holder.title_url = (TextView) convertView.findViewById(R.id.title_video);
            holder.video_url = (TextView) convertView.findViewById(R.id.url_video);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(list.get(position).getImage_url(), holder.image_url);
        holder.title_url.setText(list.get(position).getTitle());
        holder.video_url.setText(list.get(position).getVideo_url());
        return convertView;
    }

    static class ViewHolder{
        ImageView image_url;
        TextView title_url;
        TextView video_url;
    }
}
