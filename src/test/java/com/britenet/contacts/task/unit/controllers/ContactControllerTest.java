package com.britenet.contacts.task.unit.controllers;

import com.britenet.contacts.task.DTO.contact.request.update.UpdateAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdateEmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.update.UpdatePhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactWithPersonResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.controlers.ContactController;
import com.britenet.contacts.task.services.contact.ContactService;
import com.britenet.contacts.task.validators.contact.UpdateEmailAddressValidator;
import com.britenet.contacts.task.validators.contact.UpdatePhoneNumberValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.britenet.contacts.task.JsonMapper.asJson;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createAdamResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createAdrianResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarekResDTO;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ContactService contactService;

    @MockBean
    private UpdateEmailAddressValidator emailAddressValidator;

    @MockBean
    private UpdatePhoneNumberValidator phoneNumberValidator;

    @Test
    public void whenIReadAllContacts_thenIGetListOfContacts() throws Exception{
        //given
        PersonResDTO jarekResDTO = createJarekResDTO();

        ContactWithPersonResDTO jarekEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .person(jarekResDTO)
                .build();

        PersonResDTO adamResDTO = createAdamResDTO();

        ContactWithPersonResDTO adamEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("e-mail address")
                .value("adam0@o2.pl")
                .person(adamResDTO)
                .build();

        ContactWithPersonResDTO adamPhoneNumber = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("phone number")
                .value("999999999")
                .person(adamResDTO)
                .build();

        List<ContactWithPersonResDTO> contacts = Arrays.asList(jarekEmailAddress,adamEmailAddress,adamPhoneNumber);

        when(contactService.readAllContacts()).thenReturn(contacts);

        //when
        mvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].value", is(jarekEmailAddress.getValue())))
                .andExpect(jsonPath("$[0].kind", is(jarekEmailAddress.getKind())))

                .andExpect(jsonPath("$[0].person.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$[0].person.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$[0].person.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$[0].person.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$[0].person.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$[1].value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$[1].kind", is(adamEmailAddress.getKind())))

                .andExpect(jsonPath("$[1].person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$[1].person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$[1].person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$[1].person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$[1].person.pesel", is(adamResDTO.getPesel())))

                .andExpect(jsonPath("$[2].value", is(adamPhoneNumber.getValue())))
                .andExpect(jsonPath("$[2].kind", is(adamPhoneNumber.getKind())))

                .andExpect(jsonPath("$[2].person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$[2].person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$[2].person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$[2].person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$[2].person.pesel", is(adamResDTO.getPesel())));

        //then
        verify(contactService,times(1)).readAllContacts();
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIReadContact_thenIGetContact() throws Exception{
        //given
        PersonResDTO jarekResDTO = createJarekResDTO();

        ContactWithPersonResDTO jarekEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .person(jarekResDTO)
                .build();

        when(contactService.readContact(1L)).thenReturn(jarekEmailAddress);

        //when
        mvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.value", is(jarekEmailAddress.getValue())))
                .andExpect(jsonPath("$.kind", is(jarekEmailAddress.getKind())))

                .andExpect(jsonPath("$.person.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.person.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.person.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.person.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.person.pesel", is(jarekResDTO.getPesel())));

        //then
        verify(contactService,times(1)).readContact(1L);
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIReadPageOfPersons_thenIGetPageOfPersons() throws Exception{
        //given
        PersonResDTO jarekResDTO = createJarekResDTO();

        ContactWithPersonResDTO jarekEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(1L)
                .kind("e-mail address")
                .value("jery0@o2.pl")
                .person(jarekResDTO)
                .build();

        PersonResDTO adamResDTO = createAdamResDTO();

        ContactWithPersonResDTO adamEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("e-mail address")
                .value("adam0@o2.pl")
                .person(adamResDTO)
                .build();

        PersonResDTO adrianResDTO = createAdrianResDTO();

        ContactWithPersonResDTO adrianPhoneNumber = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("phone number")
                .value("999999999")
                .person(adrianResDTO)
                .build();

        List<ContactWithPersonResDTO> contacts = Arrays.asList(jarekEmailAddress,adamEmailAddress,adrianPhoneNumber);

        PageResDTO<ContactWithPersonResDTO> pageOfPersons = new PageResDTO<>(100,2,contacts);

        when(contactService.readPageOfContacts(0,3,"name","ASC")).thenReturn(pageOfPersons);

        //when
        mvc.perform(get("/api/contacts/page?number=0&size=3&sortedBy=name&order=ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(100)))
                .andExpect(jsonPath("$.currentPageNumber", is(2)))

                .andExpect(jsonPath("$.content[0].value", is(jarekEmailAddress.getValue())))
                .andExpect(jsonPath("$.content[0].kind", is(jarekEmailAddress.getKind())))

                .andExpect(jsonPath("$.content[0].person.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.content[0].person.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.content[0].person.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.content[0].person.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.content[0].person.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.content[1].value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$.content[1].kind", is(adamEmailAddress.getKind())))

                .andExpect(jsonPath("$.content[1].person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$.content[1].person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$.content[1].person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$.content[1].person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$.content[1].person.pesel", is(adamResDTO.getPesel())))

                .andExpect(jsonPath("$.content[2].value", is(adrianPhoneNumber.getValue())))
                .andExpect(jsonPath("$.content[2].kind", is(adrianPhoneNumber.getKind())))

                .andExpect(jsonPath("$.content[2].person.name", is(adrianResDTO.getName())))
                .andExpect(jsonPath("$.content[2].person.surname", is(adrianResDTO.getSurname())))
                .andExpect(jsonPath("$.content[2].person.gender", is(adrianResDTO.getGender())))
                .andExpect(jsonPath("$.content[2].person.birthDate", is(adrianResDTO.getBirthDate())))
                .andExpect(jsonPath("$.content[2].person.pesel", is(adrianResDTO.getPesel())));

        //then
        verify(contactService,times(1))
                .readPageOfContacts(0,3,"name","ASC");
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIUpdateAddress_thenIGetUpdatedContact() throws Exception {
        //given
        UpdateAddressReqDTO addressReqDTO = UpdateAddressReqDTO.subBuilder()
                .id(1L)
                .town("Zamość")
                .zipCode("23-420")
                .street("ul. 3 Maja")
                .province("lubelskie")
                .flatNumber("3")
                .blockNumber("2b")
                .build();

        PersonResDTO adamResDTO = createAdamResDTO();

        ContactWithPersonResDTO adamAddress = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("address")
                .value("ul. 3 Maja Zamość 23-420 3/2b woj. lubelskie")
                .person(adamResDTO)
                .build();

        when(contactService.updateContact(addressReqDTO)).thenReturn(adamAddress);

        //when
        mvc.perform(put("/api/addresses")
                .content(asJson(addressReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.value", is(adamAddress.getValue())))
                .andExpect(jsonPath("$.kind", is(adamAddress.getKind())))

                .andExpect(jsonPath("$.person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$.person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$.person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$.person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$.person.pesel", is(adamResDTO.getPesel())));

        //then
        verify(contactService,times(1))
                .updateContact(addressReqDTO);
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIUpdateEmailAddress_thenIGetUpdatedContact() throws Exception {
        //given
        UpdateEmailAddressReqDTO emailAddressReqDTO =
                new UpdateEmailAddressReqDTO("adam@gmail.com",1L);

        PersonResDTO adamResDTO = createAdamResDTO();

        ContactWithPersonResDTO adamEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("e-mail address")
                .value("adam@gmail.com")
                .person(adamResDTO)
                .build();

        when(emailAddressValidator.supports(any())).thenReturn(true);
        when(contactService.updateContact(emailAddressReqDTO)).thenReturn(adamEmailAddress);

        //when
        mvc.perform(put("/api/email_addresses")
                .content(asJson(emailAddressReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$.kind", is(adamEmailAddress.getKind())))

                .andExpect(jsonPath("$.person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$.person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$.person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$.person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$.person.pesel", is(adamResDTO.getPesel())));

        //then
        verify(contactService,times(1))
                .updateContact(emailAddressReqDTO);
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIUpdatePhoneNumber_thenIGetUpdatedContact() throws Exception {
        //given
        UpdatePhoneNumberReqDTO phoneNumberReqDTO =
                new UpdatePhoneNumberReqDTO("999999999",1L);

        PersonResDTO adamResDTO = createAdamResDTO();

        ContactWithPersonResDTO adamEmailAddress = ContactWithPersonResDTO.subBuilder()
                .id(2L)
                .kind("phone number")
                .value("999999999")
                .person(adamResDTO)
                .build();

        when(phoneNumberValidator.supports(any())).thenReturn(true);
        when(contactService.updateContact(phoneNumberReqDTO)).thenReturn(adamEmailAddress);

        //when
        mvc.perform(put("/api/phone_numbers")
                .content(asJson(phoneNumberReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$.kind", is(adamEmailAddress.getKind())))

                .andExpect(jsonPath("$.person.name", is(adamResDTO.getName())))
                .andExpect(jsonPath("$.person.surname", is(adamResDTO.getSurname())))
                .andExpect(jsonPath("$.person.gender", is(adamResDTO.getGender())))
                .andExpect(jsonPath("$.person.birthDate", is(adamResDTO.getBirthDate())))
                .andExpect(jsonPath("$.person.pesel", is(adamResDTO.getPesel())));

        //then
        verify(contactService,times(1))
                .updateContact(phoneNumberReqDTO);
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIDeleteContact_thenThisContactIsDeleted() throws Exception{
        //when
        mvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isOk());
        //then
        verify(contactService,times(1))
                .deleteContact(1L);
        verifyNoMoreInteractions(contactService);
    }

    @Test
    public void whenIDeleteAllPersons_thenAllPersonsAreDeleted() throws Exception{
        //when
        mvc.perform(delete("/api/contacts"))
                .andExpect(status().isOk());
        //then
        verify(contactService,times(1))
                .deleteAllContacts();
        verifyNoMoreInteractions(contactService);
    }
}
