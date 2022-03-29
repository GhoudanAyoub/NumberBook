package com.example.testret.API;

import com.example.testret.Models.Contact;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APISettings {

    @GET("all")
    Single<List<Contact>> getContact();

    @GET("telephone")
    Single<List<Contact>> getContactByPhoneNumber(
            @Query("telephone") String telephone);

    @POST("add")
    Single<Contact> AddContact(@Body Contact contact);
}
