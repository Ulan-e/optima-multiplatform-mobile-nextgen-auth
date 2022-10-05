package kz.optimabank.optima24.activity.contacts;

import static kz.optimabank.optima24.utility.Constants.CONTACT;
import static kz.optimabank.optima24.utility.Constants.COUNT_OF_ATTEMPTS;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.List;
import java.util.Objects;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.ActivitySelectContactBinding;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.activity.contacts.data.Contact;
import kz.optimabank.optima24.activity.contacts.data.ContactsRepository;
import kz.optimabank.optima24.utility.Utilities;

public class SelectContactsActivity extends OptimaActivity {

    private final static String TAG = "SelectContactsActivity";

    private ActivitySelectContactBinding binding;
    private ContactsRepository contactsRepository;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactsAdapter = new ContactsAdapter(this);
        contactsRepository = new ContactsRepository(this);

        initToolbar();
        requirePermissionAndFetchContacts();
    }

    private void requirePermissionAndFetchContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            List<Contact> contacts = contactsRepository.getContacts();
            setContacts(contacts);
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarView);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbarView.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            List<Contact> contacts = contactsRepository.getContacts();
            setContacts(contacts);
        } else {

            SharedPreferences sharedPreferences = Utilities.getPreferences(this);
            int count = sharedPreferences.getInt(COUNT_OF_ATTEMPTS, 0);

            count = count + 1;

            SharedPreferences.Editor editor = Utilities.getPreferences(this).edit();
            editor.putInt(COUNT_OF_ATTEMPTS, count);
            editor.apply();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q  && count > 2){
               final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.access_to_contacts));
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }else{
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_contact_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contactsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void setContacts(List<Contact> contacts) {
        contactsAdapter.setData(contacts);
        contactsAdapter.setClickListener(this::setResultData);
        binding.contactsRecyclerView.setAdapter(contactsAdapter);
    }

    private void setResultData(Contact contact) {
        Log.d(TAG, "setResultData " + contact);
        Intent intent = new Intent();
        intent.putExtra(CONTACT, contact);
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}