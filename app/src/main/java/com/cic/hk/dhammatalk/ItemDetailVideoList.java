package com.cic.hk.dhammatalk;

import java.util.Date;

/**
 * Created by user on 1/15/2015.
 */
public class ItemDetailVideoList {
    private String base_address_image = "http://113.212.160.12/wihara/images/";
    private String title;
    private int id;
    private String video_url;
    private String image_url;
    private Date upload_date;
    private long view_count;
    private String author;
    private String description;


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
//    public String getImage_url(){
//        return base_address_image + this.image_url;
//    }

    public void setID(int i){
        this.id= i;
    }
    public int getID(){
        return this.id;
    }

//    public void setUploadDate(Date d) {this.upload_date = d;}
//    public Date getUploadDate(){ return this.upload_date;}
//
//    public void setViewCount(long v) { this.view_count = v;}
//    public long getViewCount() { return  this.view_count;}
//
//    public void setAuthor(String a){
//        this.author = a;
//    }
//    public String getAuthor(){
//        return this.author;
//    }
    public void setDescription(String d){
        this.description = d;
    }
    public String getDescription(){
        return this.description;
    }

}
