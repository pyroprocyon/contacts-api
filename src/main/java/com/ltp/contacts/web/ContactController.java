package com.ltp.contacts.web;

import com.ltp.contacts.pojo.Contact;
import com.ltp.contacts.service.ContactService;
import com.ltp.contacts.validation.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Contact Controller", description = "REST API for managing contacts")
@RequestMapping(path = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping()
    @Operation(
            summary = "Get all contacts",
            description = "Retrieves a list of all contacts in the system"
    )
    @ApiResponse(
            responseCode = "200", description = "Successfully retrieved contacts",
            content = @Content(schema = @Schema(implementation = Contact.class, type = "array"))
    )
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok().body(contactService.getContacts());
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Get a contact by ID",
            description = "Retrieves a specific contact by their ID"
    )
    @ApiResponse(
            responseCode = "200", description = "Successfully retrieved the contact",
            content = @Content(schema = @Schema(implementation = Contact.class))
    )
    @ApiResponse(
            responseCode = "404", description = "Contact not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Contact> getContactById(@PathVariable String id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PostMapping()
    @Operation(
            summary = "Create a new contact",
            description = "Creates a new contact and returns the updated list of contacts"
    )
    @ApiResponse(
            responseCode = "201", description = "Contact successfully created",
            content = @Content(schema = @Schema(implementation = Contact.class, type = "array"))
    )
    @ApiResponse(
            responseCode = "400", description = "Invalid contact data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<Contact>> createContact(@RequestBody @Valid Contact contact) {
        contactService.saveContact(contact);
        List<Contact> contacts = contactService.getContacts();
        return ResponseEntity.status(HttpStatus.CREATED).body(contacts);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update a contact",
            description = "Updates an existing contact's information"
    )
    @ApiResponse(
            responseCode = "200", description = "Contact successfully updated",
            content = @Content(schema = @Schema(implementation = Contact.class))
    )
    @ApiResponse(
            responseCode = "404", description = "Contact not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Contact> updateContact(
            @PathVariable String id,
            @RequestBody @Valid Contact contact) {
        contact.setId(id);
        contactService.updateContact(id, contact);
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a contact",
            description = "Deletes a contact from the system"
    )
    @ApiResponse(
            responseCode = "204", description = "Contact successfully deleted"
    )
    @ApiResponse(
            responseCode = "404", description = "Contact not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}
