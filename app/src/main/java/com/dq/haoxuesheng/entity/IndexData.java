package com.dq.haoxuesheng.entity;

import java.util.List;

/**
 * 首页实体类
 * Created by jingang on 2019/1/23.
 */

public class IndexData extends EntityBase {

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
         * title : 小学生作文
         * list : [{"id":"4","title":"一年级"},{"id":"5","title":"二年级"},{"id":"6","title":"三年级"},{"id":"7","title":"四年级"},{"id":"8","title":"五年级"},{"id":"9","title":"六年级"}]
         */

        private String id;
        private String title;
        private String action;
        private List<ListBean> list;

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

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 4
             * title : 一年级
             */

            private String id;
            private String title;

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
        }
    }
}
