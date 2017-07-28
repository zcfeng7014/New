package com.cfeng.anew.bean;

import java.sql.Date;

/**
 * Created by Administrator on 2017/7/26.
 */

public class NewBean {
    private String imgurl;
    private String imgurl2;

    public NewBean() {
    }

    public NewBean(String imgurl, String imgurl2, String imgurl3, long time, String title, String source, String content, String digest) {
        this.imgurl = imgurl;
        this.imgurl2 = imgurl2;
        this.imgurl3 = imgurl3;
        this.time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time * 1000));
        this.title = title;
        this.source = source;
        this.content = content;
        this.digest = digest;
    }

    private String imgurl3;

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurl2() {
        return imgurl2;
    }

    public String getImgurl3() {
        return imgurl3;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public String getDigest() {
        return digest;
    }

    private String time;
    private String title;
    private String source;
    private String content;
    private String digest;

}
