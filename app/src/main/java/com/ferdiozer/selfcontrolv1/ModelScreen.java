package com.ferdiozer.selfcontrolv1;

public class ModelScreen {

    // Tablo ismi yazı(label)
    static final String TABLE = "screen";

    // Kolon isimleri  yazı (label)
    static final String KEY_ID = "id";
    static final String KEY_open_date = "open_date";
    static final String KEY_close_date = "close_date";


    private int id;
    private  String open_date;
    private  String close_date;


    ModelScreen(int id, String open_date, String close_date) {
        this.id = id;
        this.open_date = open_date;
        this.close_date = close_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getOpen_date() {
        return open_date;
    }

    void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    String getClose_date() {
        return close_date;
    }

    void setClose_date(String close_date) {
        this.close_date = close_date;
    }
}
