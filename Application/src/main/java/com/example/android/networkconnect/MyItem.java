package com.example.android.networkconnect;

/**
 * Created by fauxmacbook on 25/10/17.
 */

public class MyItem implements Comparable<MyItem>{
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;

    public MyItem(String n,String d, String dt, String p, String img)
    {
        name = n;
        data = d;
        date = dt;
        path = p;
        image = img;
    }
    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }
    public int compareTo(MyItem o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
