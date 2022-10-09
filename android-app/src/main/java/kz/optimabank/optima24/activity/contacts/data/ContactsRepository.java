package kz.optimabank.optima24.activity.contacts.data;

import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX_PLUS;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX_ZERO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kz.optimabank.optima24.utility.Utilities;

public class ContactsRepository {

    private final Context context;

    public ContactsRepository(Context context) {
        this.context = context;
    }

    private final static String TAG = "ContactsRepository";

    private static final String ACS_ORDER = "ASC";
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    private final List<Contact> contactList = new ArrayList<>();
    private final HashSet<String> uniquePhoneNumbers = new HashSet<>();

    public List<Contact> getContacts() {
        List<Contact> filteredContacts = new ArrayList<>();
        List<Contact> contacts = getContactListFromProvider();
        for (Contact contact : contacts) {
            String phoneNumber = contact.getPhoneNumber().trim();
            if (phoneNumber.startsWith(PHONE_NUMBER_PREFIX) ||
                    phoneNumber.startsWith(PHONE_NUMBER_PREFIX_PLUS) ||
                    (phoneNumber.startsWith(PHONE_NUMBER_PREFIX_ZERO) && phoneNumber.length() == 10)) {
                contact.setFormattedPhoneNumber(Utilities.getPhoneNumber(phoneNumber));
                filteredContacts.add(contact);
            }
        }
        return filteredContacts;
    }

    private List<Contact> getContactListFromProvider() {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " " + ACS_ORDER);

        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(nameIndex);
                    String phoneNumber = cursor.getString(numberIndex);
                    phoneNumber = phoneNumber.replace(" ", "");
                    if (!uniquePhoneNumbers.contains(phoneNumber)) {
                        contactList.add(new Contact(displayName, phoneNumber));
                        uniquePhoneNumbers.add(phoneNumber);
                        Log.d(TAG, "displayName " + displayName + " phoneNumber " + phoneNumber);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return contactList;
    }
}