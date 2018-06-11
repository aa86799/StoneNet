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
