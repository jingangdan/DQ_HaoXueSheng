package com.dq.haoxuesheng.entity;

/**
 * 实体类基类
 * Created by jingang on 2019/1/23.
 */

public class EntityBase {

    /**
     * status : 1
     * data : [{"id":"1","title":"小学生作文","list":[{"id":"4","title":"一年级"},{"id":"5","title":"二年级"},{"id":"6","title":"三年级"},{"id":"7","title":"四年级"},{"id":"8","title":"五年级"},{"id":"9","title":"六年级"}]},{"id":"2","title":"初中作文","list":[{"id":"10","title":"初一"},{"id":"11","title":"初二"},{"id":"12","title":"初三"}]},{"id":"3","title":"高中作文","list":[{"id":"13","title":"高一"},{"id":"14","title":"高二"},{"id":"15","title":"高三"}]},{"id":"0","title":"体裁作文","list":[{"id":"1","title":"写景"},{"id":"2","title":"写人"},{"id":"3","title":"叙事"},{"id":"4","title":"想象"},{"id":"5","title":"状物"}]},{"id":"0","title":"字数作文","list":[{"id":"1","title":"100字"},{"id":"2","title":"200字"}]}]
     * message :
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
