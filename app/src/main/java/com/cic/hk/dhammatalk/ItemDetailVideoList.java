package com.cic.hk.dhammatalk;

/**
 * Created by user on 1/15/2015.
 */
public class ItemDetailVideoList {
    private String base_address_image = "http://113.212.160.12/wihara/images/";
    private String title;
    private int id;
    private String video_url;
    private String image_url;
    public void setTitle(String t){
        this.title = t;
    }
    public String getTitle(){
        return this.title;
    }

    public void setVideo_url(String v){
        this.video_url = v;
    }
    public String getVideo_url(){
        return this.video_url;
    }

    public void setImage_url(String i){
        this.image_url = i;
    }
    public String getImage_url(){
        return base_address_image + this.image_url;
    }

    public void setID(int i){
        this.id= i;
    }
    public int getID(){
        return this.id;
    }
}
