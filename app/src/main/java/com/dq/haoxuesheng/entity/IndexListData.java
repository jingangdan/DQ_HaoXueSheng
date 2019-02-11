package com.dq.haoxuesheng.entity;

import java.util.List;

/**
 * 作文列表实体类
 * Created by jingang on 2019/1/23.
 */

public class IndexListData extends EntityBase{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 激动的一瞬间
         * type : 一年级
         * tizai : 写人
         * zishu : 400字
         */

        private String id;
        private String title;
        private String type;
        private String zishu;
        private String author;
        private String atime;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getZishu() {
            return zishu;
        }

        public void setZishu(String zishu) {
            this.zishu = zishu;
        }

        public String getAtime() {
            return atime;
        }

        public void setAtime(String atime) {
            this.atime = atime;
        }
    }
}
