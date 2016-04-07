package com.dalingge.gankio.bean;

import com.dalingge.gankio.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * FileName:GirlBean.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class GirlBean extends BaseBean{

    /**
     * error : false
     * results : [{"_id":"56fddfcd67765933d8be9193","_ns":"ganhuo","createdAt":"2016-04-01T10:41:17.615Z","desc":"4.1","publishedAt":"2016-04-01T11:17:05.676Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f2h04lir85j20fa0mx784.jpg","used":true,"who":"张涵宇"},{"_id":"56fc8d3a67765933d9b0a9a9","_ns":"ganhuo","createdAt":"2016-03-31T10:36:42.628Z","desc":"3.31","publishedAt":"2016-03-31T11:44:55.91Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f2fuecji0lj20f009oab3.jpg","used":true,"who":"张涵宇"},{"_id":"56fb7ca867765933d8be916d","_ns":"ganhuo","createdAt":"2016-03-30T15:13:44.353Z","desc":"3.29","publishedAt":"2016-03-30T15:17:02.228Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1f2ewruruvij20d70miadg.jpg","used":true,"who":"daimajia"},{"_id":"56f8ac1367765933d8be9154","_ns":"ganhuo","createdAt":"2016-03-28T11:59:15.439Z","desc":"3.28","publishedAt":"2016-03-29T11:56:01.215Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f2cfxa9joaj20f00fzwg2.jpg","used":true,"who":"张涵宇"},{"_id":"56f8a5b0677659164d56442f","_ns":"ganhuo","createdAt":"2016-03-28T11:32:00.491Z","desc":"3.28","publishedAt":"2016-03-28T11:43:51.83Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034gw1f2cf4ulmpzj20dw0kugn0.jpg","used":true,"who":"daimajia"},{"_id":"56f36e8b67765933d8be9133","_ns":"ganhuo","createdAt":"2016-03-24T12:35:23.841Z","desc":"3.24","publishedAt":"2016-03-25T11:23:49.570Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f27uhoko12j20ez0miq4p.jpg","used":true,"who":"张涵宇"},{"_id":"56f3697167765933dbbd20d4","_ns":"ganhuo","createdAt":"2016-03-24T12:13:37.637Z","desc":"3.24","publishedAt":"2016-03-24T12:21:54.835Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034jw1f27tuwswd3j20hs0qoq6q.jpg","used":true,"who":"daimajia"},{"_id":"56f2035767765933d8be9121","_ns":"ganhuo","createdAt":"2016-03-23T10:45:43.811Z","desc":"3.23","publishedAt":"2016-03-23T10:59:23.106Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/7a8aed7bjw1f26lox908uj20u018waov.jpg","used":true,"who":"张涵宇"},{"_id":"56f0b7d167765933dbbd20ab","_ns":"ganhuo","createdAt":"2016-03-22T11:11:13.731Z","desc":"3.22","publishedAt":"2016-03-22T11:43:32.863Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/7a8aed7bjw1f25gtggxqjj20f00b9tb5.jpg","used":true,"who":"张涵宇"},{"_id":"56ef580b67765933d9b0a91c","_ns":"ganhuo","createdAt":"2016-03-21T10:10:19.492Z","desc":"3.21","publishedAt":"2016-03-21T11:47:48.299Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f249fugof8j20hn0qogo4.jpg","used":true,"who":"张涵宇"}]
     */

    private boolean error;
    /**
     * _id : 56fddfcd67765933d8be9193
     * _ns : ganhuo
     * createdAt : 2016-04-01T10:41:17.615Z
     * desc : 4.1
     * publishedAt : 2016-04-01T11:17:05.676Z
     * source : chrome
     * type : 福利
     * url : http://ww3.sinaimg.cn/large/7a8aed7bjw1f2h04lir85j20fa0mx784.jpg
     * used : true
     * who : 张涵宇
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean extends BaseBean implements Serializable {
        private String _id;
        private String _ns;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String get_ns() {
            return _ns;
        }

        public void set_ns(String _ns) {
            this._ns = _ns;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
