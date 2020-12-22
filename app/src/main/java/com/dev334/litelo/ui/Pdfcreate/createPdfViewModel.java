package com.dev334.litelo.ui.Pdfcreate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class createPdfViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public createPdfViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}