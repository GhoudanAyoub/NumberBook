package com.example.testret.UI.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testret.Adapters.ContactAdapter;
import com.example.testret.Models.Contact;
import com.example.testret.R;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactList extends AppCompatActivity {

     public class addListenerOnTextChange implements TextWatcher {
        private Context mContext;
        TextView mEdittextview;

        public addListenerOnTextChange(Context context, TextView edittextview) {
            super();
            this.mContext = context;
            this.mEdittextview= edittextview;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            findViewById(R.id.FindPhoneButton).setVisibility(View.GONE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                editText.setError(getString(R.string.PhoneRequire));
                findViewById(R.id.FindPhoneButton).setVisibility(View.GONE);
            } else {
                editText.setError(null);
               // findViewById(R.id.FindPhoneButton).setVisibility(View.VISIBLE);
            }
            if (s.length() < 9) {
                editText.setError(getString(R.string.putYourPhoneNumber));
                //findViewById(R.id.FindPhoneButton).setVisibility(View.GONE);
            } else {
                editText.setError(null);
                findViewById(R.id.FindPhoneButton).setVisibility(View.VISIBLE);
            }
        }
    }
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private CountryCodePicker countryCodePicker;
    private EditText editText;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        _view();
        _Permission();
    }

    private void _Permission() {
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(getApplicationContext())
                        .withTitle("Contact permission")
                        .withMessage("Contact permission is needed to benefit from this app")
                        .withButtonText(getString(R.string.okay))
                        .build();
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getNumber();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        dialogPermissionListener.onPermissionDenied(permissionDeniedResponse);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    public void getNumber() {
        Map <String, String> map = new HashMap<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            map.put(name, phoneNumber);
        }
        phones.close();
        Set<String> keys = map.keySet();
        for(String key : keys){
            contactViewModel.AddContact(new Contact(key, map.get(key)));
            Log.d(key, map.get(key));
        }
    }

    private void _view() {
        recyclerView = findViewById(R.id.ContactRecyclerView);
        countryCodePicker = findViewById(R.id.ccp);
        editText = findViewById(R.id.phoneText);
        contactAdapter = new ContactAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(contactAdapter);
        findViewById(R.id.FindPhoneButton).setOnClickListener(view -> {
            phone = "+" + countryCodePicker.getSelectedCountryCode() + editText.getText().toString();
            contactViewModel.getContactByPhoneNumber(phone);
            contactViewModel.getLiveData().observe(this, contactList -> {
                contactAdapter.setList(contactList);
            });
            findViewById(R.id.FindPhoneButton).setVisibility(View.GONE);
        });
    }
}