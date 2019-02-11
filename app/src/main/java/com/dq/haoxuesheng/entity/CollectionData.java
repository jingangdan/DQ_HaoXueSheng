package com.dq.haoxuesheng.entity;

import java.util.List;

/**
 * 收藏列表实体类
 * Created by jingang on 2019/1/24.
 */

public class CollectionData extends EntityBase{

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
         * atime : 1548311728
         */

        private String id;
        private String title;
        private String atime;

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

        public String getAtime() {
            return atime;
        }

        public void setAtime(String atime) {
            this.atime = atime;
        }
    }
}
