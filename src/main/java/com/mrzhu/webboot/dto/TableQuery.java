package com.mrzhu.webboot.dto;

/**
 * 表格查询参数对象
 * Created by mrzhu on 8/29/15.
 */
public class TableQuery {
    private Integer start; //开始行数
    private Integer length; //加载行数
    private String orderColumn; //排序列index
    private String orderDir; //排序方式:asc,desc
    private String draw; //请求标记用值

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    @Override
    public String toString() {
        return "TableQuery{" +
                "start=" + start +
                ", length=" + length +
                ", orderColumn=" + orderColumn +
                ", orderDir='" + orderDir + '\'' +
                ", draw='" + draw + '\'' +
                '}';
    }
}
