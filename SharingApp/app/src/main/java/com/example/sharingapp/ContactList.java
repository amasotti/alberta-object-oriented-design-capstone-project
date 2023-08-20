package com.example.sharingapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContactList {

    private ArrayList<Contact> contacts;
    private final String FILENAME = "contacts.sav";


    public ContactList() {
        contacts = new ArrayList<Contact>();
    }


    public void setContacts(ArrayList<Contact> contact_list) {
        contacts = contact_list;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrayList<String> getAllUsernames() {
        return contacts
                .stream()
                .map(Contact::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    public Contact getContact(int index) {
        return contacts.get(index);
    }

    public int getSize() {
        return contacts.size();
    }

    public int getIndex(Contact contact) {
        return IntStream.range(0, contacts.size())
                .filter(i -> contact.getId().equals(contacts.get(i).getId()))
                .findFirst()
                .orElse(-1);
    }

    public boolean hasContact(Contact contact) {
        return contacts.stream().anyMatch(c -> c.getId().equals(contact.getId()));
    }

    public Contact getContactByUsername(String username) {
        return contacts.stream().filter(c -> c.getUsername().equals(username)).findFirst().orElse(null);
    }

    public boolean isUsernameAvailable(String username) {
        return contacts.stream().anyMatch(c -> c.getUsername().equals(username));
    }

    public void loadContacts(Context context) {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Contact>>() {
            }.getType();
            contacts = gson.fromJson(isr, listType); // temporary
            fis.close();
        } catch (IOException e) {
            contacts = new ArrayList<Contact>();
        }
    }

    public void saveContacts(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(contacts, osw);
            osw.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
