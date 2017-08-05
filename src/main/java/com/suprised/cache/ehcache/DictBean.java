package com.suprised.cache.ehcache;

import java.util.List;

public class DictBean {

    private int code;
    private String title;
    private List<DictBean> dicts;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DictBean> getDicts() {
        return dicts;
    }

    public void setDicts(List<DictBean> dicts) {
        this.dicts = dicts;
    }

    @Override
    public String toString() {
        return "DictBean [code=" + code + ", title=" + title + ", dicts={" + toStringList() + "}]";
    }
    
    public String toStringList() {
        if (this.dicts == null) return "";
        StringBuffer sb = new StringBuffer();
        sb.append("\r\n");
        for (DictBean bean : dicts) {
            sb.append(bean);
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
