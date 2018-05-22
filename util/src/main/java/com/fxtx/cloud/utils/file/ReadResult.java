package com.fxtx.cloud.utils.file;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nldjy on 2015/10/9.
 */
public class ReadResult<E> {
    private boolean isErr ;
    private List<String> errList ;
    private List<E> dataList ;
    public boolean isErr(){
        return isErr ;
    }
    public ReadResult(){
        this.errList = new ArrayList<String>();
        this.dataList = new ArrayList<E>() ;
        this.isErr = false ;
    }
    public void addErr(String errMsg){
        this.isErr = true ;
        errMsg = errMsg.replaceAll("^errData","") ;
        this.errList.add(errMsg) ;
    }
    public void addData(E entity){
        this.dataList.add(entity) ;
    }
    public List<String> getErrList(){
        return this.errList;
    }
    public List<E> getDataList(){
        return this.dataList ;
    }
}
