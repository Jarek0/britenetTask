package com.britenet.contacts.task.controlers;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.services.person.PersonService;
import com.britenet.contacts.task.validators.contact.EmailAddressValidator;
import com.britenet.contacts.task.validators.contact.PhoneNumberValidator;
import com.britenet.contacts.task.validators.contact.UpdatePhoneNumberValidator;
import com.britenet.contacts.task.validators.person.PersonReqDTOValidatorImpl;
import com.britenet.contacts.task.validators.person.UpdatePersonReqDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    private final PersonReqDTOValidatorImpl personReqDTOValidator;

    private final UpdatePersonReqDTOValidator updatePersonReqDTOValidator;

    private final EmailAddressValidator emailAddressValidator;

    private final PhoneNumberValidator phoneNumberValidator;

    public PersonController(PersonService personService,
                            PersonReqDTOValidatorImpl personReqDTOValidator,
                            UpdatePersonReqDTOValidator updatePersonReqDTOValidator,
                            EmailAddressValidator emailAddressValidator,
                            PhoneNumberValidator phoneNumberValidator){
        this.personService = personService;
        this.personReqDTOValidator = personReqDTOValidator;
        this.updatePersonReqDTOValidator = updatePersonReqDTOValidator;
        this.emailAddressValidator = emailAddressValidator;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    @InitBinder("personReqDTO")
    public void setValidatorForPersonReqDTO(WebDataBinder binder){
        binder.addValidators(personReqDTOValidator);
    }

    @InitBinder("updatePersonReqDTO")
    public void setValidatorForUpdatePersonReqDTO(WebDataBinder binder){
        binder.addValidators(updatePersonReqDTOValidator);
    }

    @InitBinder("emailAddressReqDTO")
    public void setValidatorForEmailAddressReqDTO(WebDataBinder binder){
        binder.addValidators(emailAddressValidator);
    }

    @InitBinder("phoneNumberReqDTO")
    public void setValidatorForPhoneNumberReqDTO(WebDataBinder binder){
        binder.addValidators(phoneNumberValidator);
    }

    @PostMapping
    public ResponseEntity<PersonResDTO> createPerson(@RequestBody @Valid PersonReqDTO personReqDTO){
        return new ResponseEntity<>(personService.createPerson(personReqDTO), HttpStatus.OK);
    }

    @PostMapping("/{personId}/addresses")
    public ResponseEntity<PersonWithContactsResDTO> createContactForPerson(@PathVariable long personId,
                                                                           @RequestBody @Valid AddressReqDTO addressReqDTO){
        return new ResponseEntity<>(personService.createContactForPerson(personId,addressReqDTO), HttpStatus.OK);
    }

    @PostMapping("/{personId}/email_addresses")
    public ResponseEntity<PersonWithContactsResDTO> createContactForPerson(@PathVariable long personId,
                                                                           @RequestBody @Valid EmailAddressReqDTO emailAddressReqDTO){
        return new ResponseEntity<>(personService.createContactForPerson(personId,emailAddressReqDTO), HttpStatus.OK);
    }

    @PostMapping("/{personId}/phone_numbers")
    public ResponseEntity<PersonWithContactsResDTO> createContactForPerson(@PathVariable long personId,
                                                                           @RequestBody @Valid PhoneNumberReqDTO phoneNumberReqDTO){
        return new ResponseEntity<>(personService.createContactForPerson(personId,phoneNumberReqDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PersonResDTO>> readAllPersons(){
        return new ResponseEntity<>(personService.readAllPersons(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<PageResDTO<PersonResDTO>> readPageOfPersons(@RequestParam int number,
                                                      @RequestParam int size,
                                                      @RequestParam String sortedBy,
                                                      @RequestParam String order){
        return new ResponseEntity<>(personService.readPageOfPersons(number,size,sortedBy,order), HttpStatus.OK);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<PersonWithContactsResDTO>> readAllPersonsWithContacts(){
        return new ResponseEntity<>(personService.readAllPersonsWithContacts(), HttpStatus.OK);
    }

    @GetMapping("/contacts/page")
    public ResponseEntity<PageResDTO<PersonWithContactsResDTO>> readPageOfPersonsWithContacts(@RequestParam int number,
                                                                              @RequestParam int size,
                                                                              @RequestParam String sortedBy,
                                                                              @RequestParam String order){
        return new ResponseEntity<>(personService.readPageOfPersonsWithContacts(number,size,sortedBy,order), HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonResDTO> readPerson(@PathVariable long personId){
        return new ResponseEntity<>(personService.readPerson(personId), HttpStatus.OK);
    }

    @GetMapping("/pesel/{pesel}")
    public ResponseEntity<PersonResDTO> readPersonByPesel(@PathVariable String pesel){
        return new ResponseEntity<>(personService.readPersonByPesel(pesel), HttpStatus.OK);
    }

    @GetMapping("/{personId}/contacts")
    public ResponseEntity<PersonWithContactsResDTO> readPersonWithContacts(@PathVariable long personId){
        return new ResponseEntity<>(personService.readPersonWithContacts(personId), HttpStatus.OK);
    }

    @GetMapping("/pesel/{pesel}/contacts")
    public ResponseEntity<PersonResDTO> readPersonByPeselWithContacts(@PathVariable String pesel){
        return new ResponseEntity<>(personService.readPersonByPeselWithContacts(pesel), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<PersonResDTO> updatePerson(@RequestBody @Valid UpdatePersonReqDTO updatePersonReqDTO){
        return new ResponseEntity<>(personService.updatePerson(updatePersonReqDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{personId}")
    public void deletePerson(@PathVariable long personId){
        personService.deletePerson(personId);
    }

    @DeleteMapping
    public void deleteAllPersons(){
        personService.deleteAllPersons();
    }
}
