package com.stone.stonenet.bean;

import java.io.Serializable;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 01/02/2018 14 44
 */
public class BaseRxBean implements Serializable {
    /* 符合 RESTFUL 的 json 格式，所有 bean 都应该继承自该 bean
     */

    public int status = -1;
    public String  msg;
    public long curtime;

    public boolean isSuccess() {
        return status == 0;
    }

    @Override
    public String toString() {
        return "BaseResult [status=" + status + ", msg=" + msg + "]";
    }
}
