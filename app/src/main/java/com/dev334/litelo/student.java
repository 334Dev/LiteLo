package com.dev334.litelo;

public class student {
    private String subname;
    private int Present;
    private int Absent;

    public student(String subname, int present, int absent) {
        this.subname = subname;
        Present = present;
        Absent = absent;
    }
    public student()
    {

    }

    public String getSubname() {
        return subname;
    }

    public int getPresent() {
        return Present;
    }

    public int getAbsent() {
        return Absent;
    }
}
