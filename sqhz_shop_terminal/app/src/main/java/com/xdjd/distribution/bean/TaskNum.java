package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/4
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class TaskNum extends BaseBean {

    private String taskNum = "0";//	任务数量
    private String taskNumCus = "0";//	配送出库数量

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getTaskNumCus() {
        return taskNumCus;
    }

    public void setTaskNumCus(String taskNumCus) {
        this.taskNumCus = taskNumCus;
    }
}
