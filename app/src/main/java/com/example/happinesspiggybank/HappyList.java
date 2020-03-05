package com.example.happinesspiggybank;

//HappyList 클래스 정의
public class HappyList {
    private int id;
    private String date;
    private String time;
    private String cont;

    public HappyList(int id, String date, String time, String cont) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.cont = cont;
    }

    public HappyList() {
        this.id = 0;
        this.date = null;
        this.time = null;
        this.cont = null;
    }

    public int getId() { return this.id; }
    public String getDate() { return this.date; }
    public String getTime() { return this.time; }
    public String getCont() { return this.cont; }

    public void setId(int i) { this.id = i; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setContent(String cont) { this.cont = cont; }

}