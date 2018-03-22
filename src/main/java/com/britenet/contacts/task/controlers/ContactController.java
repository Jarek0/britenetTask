package com.britenet.contacts.task.controlers;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.aspect.LogExecutionTime;
import com.britenet.contacts.task.services.contact.ContactService;
import com.britenet.contacts.task.validators.contact.UpdateEmailAddressValidator;
import com.britenet.contacts.task.validators.contact.UpdatePhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactService;

    private final UpdateEmailAddressValidator emailAddressValidator;

    private final UpdatePhoneNumberValidator phoneNumberValidator;

    @Autowired
    public ContactController(ContactService contactService, UpdateEmailAddressValidator emailAddressValidator, UpdatePhoneNumberValidator phoneNumberValidator){
        this.contactService = contactService;
        this.emailAddressValidator = emailAddressValidator;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    @InitBinder("emailAddressReqDTO")
    public void setValidatorForEmailRequest(WebDataBinder binder){
        binder.addValidators(emailAddressValidator);
    }

    @InitBinder("phoneNumberReqDTO")
    public void setValidatorForPhoneNumberRequest(WebDataBinder binder){
        binder.addValidators(phoneNumberValidator);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactWithPersonResDTO>> readAllContacts(){
        return new ResponseEntity<>(contactService.readAllContacts(), HttpStatus.OK);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<ContactWithPersonResDTO> readContact(@PathVariable long contactId){
        return new ResponseEntity<>(contactService.readContact(contactId), HttpStatus.OK);
    }

    @GetMapping("/contacts/page")
    public ResponseEntity<PageResDTO<ContactWithPersonResDTO>> readPageOfContacts(@RequestParam int number,
                                                                  @RequestParam int size,
                                                                  @RequestParam String sortedBy,
                                                                  @RequestParam String order){
        return new ResponseEntity<>(contactService.readPageOfContacts(number,size,sortedBy,order), HttpStatus.OK);
    }

    @PutMapping("/addresses")
    public ResponseEntity<ContactWithPersonResDTO> updateContact(@RequestBody @Valid UpdateAddressReqDTO addressReqDTO){
        return new ResponseEntity<>(contactService.updateContact(addressReqDTO), HttpStatus.OK);
    }

    @PutMapping("/email_addresses")
    public ResponseEntity<ContactWithPersonResDTO> updateContact(@RequestBody @Valid UpdateEmailAddressReqDTO emailAddressReqDTO){
        return new ResponseEntity<>(contactService.updateContact(emailAddressReqDTO), HttpStatus.OK);
    }

    @PutMapping("/phone_numbers")
    public ResponseEntity<ContactWithPersonResDTO> updateContact(@RequestBody @Valid UpdatePhoneNumberReqDTO phoneNumberReqDTO){
        return new ResponseEntity<>(contactService.updateContact(phoneNumberReqDTO), HttpStatus.OK);
    }

    @DeleteMapping("/contacts/{contactId}")
    public void deleteContact(@PathVariable long contactId){
        contactService.deleteContact(contactId);
    }

    @DeleteMapping("/contacts")
    public void deleteAllContacts(){
        contactService.deleteAllContacts();
    }
}
