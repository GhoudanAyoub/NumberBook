package com.example.testret.API;

import com.example.testret.Models.Contact;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Response;

public class Repository {
    private final APISettings apiSettings;

    @Inject
    public Repository(APISettings apiSettings) {
        this.apiSettings = apiSettings;
    }

    public Single<List<Contact>> getContact() {
        return apiSettings.getContact();
    }

    public Single<List<Contact>> getContactByPhoneNumber(String telephone) {
        return apiSettings.getContactByPhoneNumber(telephone);
    }

    public Single<Contact> AddContact(Contact contact) {
       return apiSettings.AddContact(contact);
    }
}
