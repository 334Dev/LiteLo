package com.example.litelo;

public class resourceModel {
    String Resname,link;
    
    public resourceModel(){
        //
    }


    public String getResname() {
        return Resname;
    }

    public void setResname(String resname) {
        Resname = resname;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public resourceModel(String Resname, String link) {
        this.Resname = Resname;
        this.link = link;
    }
}
