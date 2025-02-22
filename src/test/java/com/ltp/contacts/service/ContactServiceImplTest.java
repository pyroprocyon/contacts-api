package com.ltp.contacts.service;

import com.ltp.contacts.pojo.Contact;
import com.ltp.contacts.repository.ContactRepository;
import com.ltp.contacts.validation.ContactNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void testGetContactByIdSuccessfully() {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setPhoneNumber("1234567890");

        List<Contact> contactList = List.of(contact);

        when(contactRepository.getContacts()).thenReturn(contactList);
        when(contactRepository.getContact(0)).thenReturn(contact);

        String contactId = contact.getId();
        Contact retrievedContact = contactService.getContactById(contactId);

        assertNotNull(retrievedContact);
        assertEquals(contactId, retrievedContact.getId());
        assertEquals("John Doe", retrievedContact.getName());
        assertEquals("1234567890", retrievedContact.getPhoneNumber());
    }

    @Test
    void testGetContactByIdThrowsExceptionWhenNotFound() {
        String nonExistentId = UUID.randomUUID().toString();

        when(contactRepository.getContacts()).thenReturn(new ArrayList<>());

        assertThrows(ContactNotFoundException.class,
                () -> contactService.getContactById(nonExistentId));
    }

}
