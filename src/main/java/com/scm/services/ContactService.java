package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    //save contact
    Contact saveContact(Contact contact);

    //update contact
    Contact updateContact(Contact contact);

    //set all contacts
    List <Contact> getAllContacts();

    //deletwe contact
    void deleteContact(String id);

    //get contact by Id
    Contact getContactById(String id); 

    //search contact
    Page <Contact> searchByName(String nameKeyword, int size, int page, String sortBy , String direction, User user);
    Page <Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy , String direction, User user);
    Page <Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy , String direction, User user);


    //get contact by user id
    List <Contact> getByUserId(String userId);

    //pagenation
    Page <Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);


}
