package com.example.litelo;

import com.google.firebase.firestore.DocumentId;

public class subjectModel {
    private Integer Absent, Present;

    @DocumentId
    private String documentId;


    public subjectModel(){
        //empty constructor
    }

    public subjectModel(Integer absent, Integer present, String documentId) {
        Absent = absent;
        Present = present;
        this.documentId=documentId;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getAbsent() {
        return Absent;
    }

    public void setAbsent(Integer absent) {
        Absent = absent;
    }

    public Integer getPresent() {
        return Present;
    }

    public void setPresent(Integer present) {
        Present = present;
    }
}
