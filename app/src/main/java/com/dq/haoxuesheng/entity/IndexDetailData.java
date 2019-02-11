package com.dq.haoxuesheng.entity;

import java.util.List;

/**
 * 作文详情实体类
 * Created by jingang on 2019/1/24.
 */

public class IndexDetailData extends EntityBase {

    /**
     * data : {"id":"1","title":"激动的一瞬间","tid":"1","lid":"4","tj":"0","author":"","tzid":"3","zsid":"4","type":"小学生作文","tizai":"叙事","zishu":"400字","hits":"","ding":"","islike":false,"imglist":["http://hxszw.dequanhuibao.com/uploads/test/1.jpg","http://hxszw.dequanhuibao.com/uploads/test/2.jpg"],"content":"今天，老师宣布了期中考试前十名。我紧张极了。因为我做为中队委；做为老师眼中的好学生；做为妈妈的乖女儿，如果没有进前十，那脸就丢尽了！<br />\r\n老师的话语刚刚响起，我的心就开始狂跳个不停。同学们都坐得笔直，唯独我的手在抖、脚在不安地踢着地面。一个、两个\u2026\u2026许多同学都被念到了，还是没有我。环顾四周，被念到的同学红光满面，没有被点到的，也只能坐着干着急。而我，心已经跳到了嗓子眼：我该怎么办啊？！真希望我的名字不要在名单中\u201c离家出走\u201d。天气虽然很冷，我的脑门上却渗出了一层密密的汗珠。<br />\r\n仿佛过了一个世纪之久，老师才念完了九个。第十名，一定没我的份儿。我想。这时，泪花已在我眼睛里打转了，真想痛快地大哭一场！\u201c第十名，子逸！\u201d老师的声音猛然响起。我把垂得低低的头抬起来，难以置信地望着老师，哦！没错，老师在对我微笑，这微笑，像一只无形的但有力量的手，把我从低谷拉到云端\u2026\u2026<br />\r\n在那一刻，我哭了、我笑了、我激动了。这只是第十名，我要再接再厉、创造奇迹！"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 激动的一瞬间
         * tid : 1
         * lid : 4
         * tj : 0
         * author :
         * tzid : 3
         * zsid : 4
         * type : 小学生作文
         * tizai : 叙事
         * zishu : 400字
         * hits :
         * ding :
         * islike : false
         * imglist : ["http://hxszw.dequanhuibao.com/uploads/test/1.jpg","http://hxszw.dequanhuibao.com/uploads/test/2.jpg"]
         * content : 今天，老师宣布了期中考试前十名。我紧张极了。因为我做为中队委；做为老师眼中的好学生；做为妈妈的乖女儿，如果没有进前十，那脸就丢尽了！<br />
         * 老师的话语刚刚响起，我的心就开始狂跳个不停。同学们都坐得笔直，唯独我的手在抖、脚在不安地踢着地面。一个、两个……许多同学都被念到了，还是没有我。环顾四周，被念到的同学红光满面，没有被点到的，也只能坐着干着急。而我，心已经跳到了嗓子眼：我该怎么办啊？！真希望我的名字不要在名单中“离家出走”。天气虽然很冷，我的脑门上却渗出了一层密密的汗珠。<br />
         * 仿佛过了一个世纪之久，老师才念完了九个。第十名，一定没我的份儿。我想。这时，泪花已在我眼睛里打转了，真想痛快地大哭一场！“第十名，子逸！”老师的声音猛然响起。我把垂得低低的头抬起来，难以置信地望着老师，哦！没错，老师在对我微笑，这微笑，像一只无形的但有力量的手，把我从低谷拉到云端……<br />
         * 在那一刻，我哭了、我笑了、我激动了。这只是第十名，我要再接再厉、创造奇迹！
         */

        private String id;
        private String title;
        private String tid;
        private String lid;
        private String tj;
        private String author;
        private String tzid;
        private String zsid;
        private String type;
        private String tizai;
        private String zishu;
        private String hits;
        private String ding;
        private boolean iscang;
        private String content;
        private List<String> imglist;

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

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
        }

        public String getTj() {
            return tj;
        }

        public void setTj(String tj) {
            this.tj = tj;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTzid() {
            return tzid;
        }

        public void setTzid(String tzid) {
            this.tzid = tzid;
        }

        public String getZsid() {
            return zsid;
        }

        public void setZsid(String zsid) {
            this.zsid = zsid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTizai() {
            return tizai;
        }

        public void setTizai(String tizai) {
            this.tizai = tizai;
        }

        public String getZishu() {
            return zishu;
        }

        public void setZishu(String zishu) {
            this.zishu = zishu;
        }

        public String getHits() {
            return hits;
        }

        public void setHits(String hits) {
            this.hits = hits;
        }

        public String getDing() {
            return ding;
        }

        public void setDing(String ding) {
            this.ding = ding;
        }

        public boolean isIscang() {
            return iscang;
        }

        public void setIscang(boolean iscang) {
            this.iscang = iscang;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImglist() {
            return imglist;
        }

        public void setImglist(List<String> imglist) {
            this.imglist = imglist;
        }

        public String getAtime() {
            return atime;
        }

        public void setAtime(String atime) {
            this.atime = atime;
        }
    }
}
