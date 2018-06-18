package com.gameweb.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;

public class SortParams {

    double fromRat;
    double toRat;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date fromDat;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date toDat;
    int tag;
    int sort;



    public SortParams(){
        this.fromRat = 3;
        this.toRat = 5;
        this.fromDat = new Date(1900-01-01);
        this.toDat = new Date(2019-12-31);
        this.tag = 0;
        this.sort = 1;
    }
    public SortParams(double fromRat, double toRat, Date fromDat, Date toDat, int tag, int sort) {
        this.fromRat = fromRat;
        this.toRat = toRat;
        this.fromDat = fromDat;
        this.toDat = toDat;
        this.tag = tag;
        this.sort = sort;
    }

    public double getFromRat() {
        return fromRat;
    }

    public void setFromRat(double fromRat) {
        this.fromRat = fromRat;
    }

    public double getToRat() {
        return toRat;
    }

    public void setToRat(double toRat) {
        this.toRat = toRat;
    }

    public Date getFromDat() {
        return fromDat;
    }

    public void setFromDat(Date fromDat) {
        this.fromDat = fromDat;
    }

    public Date getToDat() {
        return toDat;
    }

    public void setToDat(Date toDat) {
        this.toDat = toDat;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
