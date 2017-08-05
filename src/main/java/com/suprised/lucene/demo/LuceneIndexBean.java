package com.suprised.lucene.demo;

import java.util.Date;

/**
 */
public class LuceneIndexBean {

    private String key;

    private String title;
    
    private String url;

    private Date date;
    
    private String content;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LuceneIndexBean [key=" + key + ", title=" + title + ", url=" + url + ", date=" + date + ", content=" + content
            + "]";
    }
    
}
