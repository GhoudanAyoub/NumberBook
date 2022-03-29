package com.example.testret.UI.main;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testret.API.Repository;
import com.example.testret.Models.Contact;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class ContactViewModel extends ViewModel {
    private final Repository repository;

    @ViewModelInject
    public ContactViewModel(Repository repository) {
        this.repository = repository;
    }

    private MutableLiveData<List<Contact>> liveData = new MutableLiveData<>();

    public LiveData<List<Contact>> getLiveData() {
        if (liveData == null)
            liveData = new MutableLiveData<>();
        return liveData;
    }

    public void getContact() {
        repository.getContact()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactData ->
                                liveData.setValue(contactData)
                        , Throwable::printStackTrace);
    }
    public void getContactByPhoneNumber(String telephone) {
        repository.getContactByPhoneNumber(telephone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactData ->
                                liveData.setValue(contactData)
                        , Throwable::printStackTrace);
    }

    public void AddContact(Contact contact) {
        repository.AddContact(contact)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                                Log.d("TAG", "AddContact: "+response.getNom())
                                        , Throwable::printStackTrace);
    }
}
