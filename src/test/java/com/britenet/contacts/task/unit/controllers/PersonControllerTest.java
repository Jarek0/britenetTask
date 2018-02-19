package com.britenet.contacts.task.unit.controllers;

import com.britenet.contacts.task.DTO.contact.request.create.AddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.EmailAddressReqDTO;
import com.britenet.contacts.task.DTO.contact.request.create.PhoneNumberReqDTO;
import com.britenet.contacts.task.DTO.contact.response.ContactResDTO;
import com.britenet.contacts.task.DTO.page.PageResDTO;
import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.request.UpdatePersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.DTO.person.response.PersonWithContactsResDTO;
import com.britenet.contacts.task.controlers.PersonController;
import com.britenet.contacts.task.services.person.PersonService;
import com.britenet.contacts.task.validators.contact.EmailAddressValidator;
import com.britenet.contacts.task.validators.contact.PhoneNumberValidator;
import com.britenet.contacts.task.validators.person.PersonReqDTOValidatorImpl;
import com.britenet.contacts.task.validators.person.UpdatePersonReqDTOValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.britenet.contacts.task.JsonMapper.asJson;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressReqDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestAddressFactory.createTestAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestEmailAddressFactory.createTestEmailAddressResDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.*;
import static com.britenet.contacts.task.testObjectsFactories.TestPhoneNumberFactory.createTestPhoneNumberResDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonReqDTOValidatorImpl personReqDTOValidator;

    @MockBean
    private UpdatePersonReqDTOValidator updatePersonReqDTOValidator;

    @MockBean
    private EmailAddressValidator emailAddressValidator;

    @MockBean
    private PhoneNumberValidator phoneNumberValidator;

    @Test
    public void whenICreatePerson_thenIGetCreatedPerson() throws Exception {
        //given
        PersonReqDTO personReqDTO = createJarekReqDTO();
        PersonResDTO personResDTO = createJarekResDTO();

        when(personReqDTOValidator.supports(any())).thenReturn(true);
        when(personService.createPerson(personReqDTO)).thenReturn(personResDTO);

        //when
        mvc.perform(post("/api/persons")
                .content(asJson(personReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(personResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(personResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(personResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(personResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(personResDTO.getPesel())));

        //then
        verify(personService,times(1)).createPerson(personReqDTO);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenITryToCreatePersonWithInvalidData_thenIGetError() throws Exception {
        //given
        PersonReqDTO personReqDTO = PersonReqDTO.builder()
                .name("jarek")
                .surname("b")
                .gender("invalid")
                .birthDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
                .pesel("999999999999")
                .build();
        PersonResDTO personResDTO = createJarekResDTO();

        when(personReqDTOValidator.supports(any())).thenReturn(true);
        when(personService.createPerson(personReqDTO)).thenReturn(personResDTO);

        //when
        mvc.perform(post("/api/persons")
                .content(asJson(personReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.fieldErrors[*]", hasSize(5)));

        //then
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreateAddressForPerson_thenIGetPersonWithCreatedContact() throws Exception {
        //given
        AddressReqDTO jarekAddressReqDTO = createTestAddressReqDTO();

        ContactResDTO address = createTestAddressResDTO();

        PersonWithContactsResDTO jarekResDTO = PersonWithContactsResDTO.subBuilder()
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-01-01")
                .pesel("99999999999")
                .contacts(Collections.singletonList(address))
                .build();

        when(personService.createContactForPerson(1L,jarekAddressReqDTO)).thenReturn(jarekResDTO);

        //when
        mvc.perform(post("/api/persons/1/addresses")
                .content(asJson(jarekAddressReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.contacts[0].kind", is(address.getKind())))
                .andExpect(jsonPath("$.contacts[0].value", is(address.getValue())));

        //then
        verify(personService,times(1)).createContactForPerson(1L,jarekAddressReqDTO);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreateAddressWithInvalidDataForPerson_thenIGetError() throws Exception {
        //given
        AddressReqDTO invalidAddress = AddressReqDTO.builder()
                .town("lublin")
                .zipCode("222-420")
                .street("alejka Tomasza Zana")
                .province("invalid")
                .flatNumber("2a")
                .blockNumber("a2a")
                .build();

        //when
        mvc.perform(post("/api/persons/1/addresses")
                .content(asJson(invalidAddress))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.fieldErrors[*]", hasSize(6)));

        //then
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreateEmailAddressForPerson_thenIGetPersonWithCreatedContact() throws Exception {
        //given
        EmailAddressReqDTO jarekEmailAddressReqDTO = new EmailAddressReqDTO("jery0@o2.pl");

        ContactResDTO emailAddress = createTestEmailAddressResDTO();

        PersonWithContactsResDTO jarekResDTO = PersonWithContactsResDTO.subBuilder()
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-01-01")
                .pesel("99999999999")
                .contacts(Collections.singletonList(emailAddress))
                .build();

        when(emailAddressValidator.supports(any())).thenReturn(true);
        when(personService.createContactForPerson(1L,jarekEmailAddressReqDTO)).thenReturn(jarekResDTO);

        //when
        mvc.perform(post("/api/persons/1/email_addresses")
                .content(asJson(jarekEmailAddressReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.contacts[0].kind", is(emailAddress.getKind())))
                .andExpect(jsonPath("$.contacts[0].value", is(emailAddress.getValue())));

        //then
        verify(personService,times(1)).createContactForPerson(1L,jarekEmailAddressReqDTO);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreateEmailAddressWithInvalidDataForPerson_thenIGetError() throws Exception {
        //given
        EmailAddressReqDTO invalidEmailAddress = new EmailAddressReqDTO("jery0o2.pl");

        when(emailAddressValidator.supports(any())).thenReturn(true);

        //when
        mvc.perform(post("/api/persons/1/email_addresses")
                .content(asJson(invalidEmailAddress))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.fieldErrors[*]", hasSize(1)));

        //then
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreatePhoneNumberForPerson_thenIGetPersonWithCreatedContact() throws Exception {
        //given
        PhoneNumberReqDTO jarekPhoneNumber = new PhoneNumberReqDTO("999999999");

        ContactResDTO phoneNumber = createTestPhoneNumberResDTO();

        PersonWithContactsResDTO jarekResDTO = PersonWithContactsResDTO.subBuilder()
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-01-01")
                .pesel("99999999999")
                .contacts(Collections.singletonList(phoneNumber))
                .build();

        when(phoneNumberValidator.supports(any())).thenReturn(true);
        when(personService.createContactForPerson(1L,jarekPhoneNumber)).thenReturn(jarekResDTO);

        //when
        mvc.perform(post("/api/persons/1/phone_numbers")
                .content(asJson(jarekPhoneNumber))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.contacts[0].kind", is(phoneNumber.getKind())))
                .andExpect(jsonPath("$.contacts[0].value", is(phoneNumber.getValue())));

        //then
        verify(personService,times(1)).createContactForPerson(1L,jarekPhoneNumber);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenICreatePhoneNumberWithInvalidDataForPerson_thenIGetError() throws Exception {
        //given
        PhoneNumberReqDTO invalidPhoneNumber = new PhoneNumberReqDTO("9999999");

        when(phoneNumberValidator.supports(any())).thenReturn(true);

        //when
        mvc.perform(post("/api/persons/1/phone_numbers")
                .content(asJson(invalidPhoneNumber))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.fieldErrors[*]", hasSize(1)));

        //then
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadAllPersons_thenIGetListOfPersons() throws Exception {
        //given
        PersonResDTO jarek = createJarekResDTO();
        PersonResDTO adam = createAdamResDTO();
        PersonResDTO adrian = createAdrianResDTO();

        List<PersonResDTO> people = Arrays.asList(jarek,adam,adrian);

        when(personService.readAllPersons()).thenReturn(people);

        //when
        mvc.perform(get("/api/persons/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(jarek.getName())))
                .andExpect(jsonPath("$[0].surname", is(jarek.getSurname())))
                .andExpect(jsonPath("$[0].gender", is(jarek.getGender())))
                .andExpect(jsonPath("$[0].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$[0].pesel", is(jarek.getPesel())))

                .andExpect(jsonPath("$[1].name", is(adam.getName())))
                .andExpect(jsonPath("$[1].surname", is(adam.getSurname())))
                .andExpect(jsonPath("$[1].gender", is(adam.getGender())))
                .andExpect(jsonPath("$[1].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$[1].pesel", is(adam.getPesel())))

                .andExpect(jsonPath("$[2].name", is(adrian.getName())))
                .andExpect(jsonPath("$[2].surname", is(adrian.getSurname())))
                .andExpect(jsonPath("$[2].gender", is(adrian.getGender())))
                .andExpect(jsonPath("$[2].birthDate", is(adrian.getBirthDate())))
                .andExpect(jsonPath("$[2].pesel", is(adrian.getPesel())));

        //then
        verify(personService,times(1)).readAllPersons();
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadPageOfPersons_thenIGetPageOfPersons() throws Exception{
        //given
        PersonResDTO jarek = createJarekResDTO();
        PersonResDTO adam = createAdamResDTO();
        PersonResDTO adrian = createAdrianResDTO();

        List<PersonResDTO> people = Arrays.asList(adam,adrian,jarek);

        PageResDTO<PersonResDTO> pageOfPersons = new PageResDTO<>(100,1,people);

        when(personService.readPageOfPersons(0,3,"name","ASC")).thenReturn(pageOfPersons);

        //when
        mvc.perform(get("/api/persons/page?number=0&size=3&sortedBy=name&order=ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(100)))
                .andExpect(jsonPath("$.currentPageNumber", is(1)))

                .andExpect(jsonPath("$.content[0].name", is(adam.getName())))
                .andExpect(jsonPath("$.content[0].surname", is(adam.getSurname())))
                .andExpect(jsonPath("$.content[0].gender", is(adam.getGender())))
                .andExpect(jsonPath("$.content[0].birthDate", is(adam.getBirthDate())))
                .andExpect(jsonPath("$.content[0].pesel", is(adam.getPesel())))

                .andExpect(jsonPath("$.content[1].name", is(adrian.getName())))
                .andExpect(jsonPath("$.content[1].surname", is(adrian.getSurname())))
                .andExpect(jsonPath("$.content[1].gender", is(adrian.getGender())))
                .andExpect(jsonPath("$.content[1].birthDate", is(adrian.getBirthDate())))
                .andExpect(jsonPath("$.content[1].pesel", is(adrian.getPesel())))

                .andExpect(jsonPath("$.content[2].name", is(jarek.getName())))
                .andExpect(jsonPath("$.content[2].surname", is(jarek.getSurname())))
                .andExpect(jsonPath("$.content[2].gender", is(jarek.getGender())))
                .andExpect(jsonPath("$.content[2].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$.content[2].pesel", is(jarek.getPesel())));

        //then
        verify(personService,times(1)).readPageOfPersons(0,3,"name","ASC");
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadAllPersonsWithContacts_thenIGetListOfPersonsWithContacts() throws Exception {
        //given

        ContactResDTO jarekPhoneNumber = createTestPhoneNumberResDTO();

        PersonWithContactsResDTO jarek = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Collections.singletonList(jarekPhoneNumber))
                .build();

        ContactResDTO adamEmailAddress = createTestEmailAddressResDTO();

        PersonWithContactsResDTO adam = PersonWithContactsResDTO.subBuilder()
                .name("Adam")
                .surname("Kowalski")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999998")
                .contacts(Collections.singletonList(adamEmailAddress))
                .build();

        List<PersonWithContactsResDTO> people = Arrays.asList(jarek,adam);

        when(personService.readAllPersonsWithContacts()).thenReturn(people);

        //when
        mvc.perform(get("/api/persons/contacts"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name", is(jarek.getName())))
                .andExpect(jsonPath("$[0].surname", is(jarek.getSurname())))
                .andExpect(jsonPath("$[0].gender", is(jarek.getGender())))
                .andExpect(jsonPath("$[0].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$[0].pesel", is(jarek.getPesel())))

                .andExpect(jsonPath("$[0].contacts[0].value", is(jarekPhoneNumber.getValue())))
                .andExpect(jsonPath("$[0].contacts[0].kind", is(jarekPhoneNumber.getKind())))

                .andExpect(jsonPath("$[1].name", is(adam.getName())))
                .andExpect(jsonPath("$[1].surname", is(adam.getSurname())))
                .andExpect(jsonPath("$[1].gender", is(adam.getGender())))
                .andExpect(jsonPath("$[1].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$[1].pesel", is(adam.getPesel())))

                .andExpect(jsonPath("$[1].contacts[0].value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$[1].contacts[0].kind", is(adamEmailAddress.getKind())));

        //then
        verify(personService,times(1)).readAllPersonsWithContacts();
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadPageOfPersonsWithContacts_thenIGenPageOfPersonsWithContacts() throws Exception {
        //given

        ContactResDTO jarekPhoneNumber = createTestPhoneNumberResDTO();

        PersonWithContactsResDTO jarek = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Collections.singletonList(jarekPhoneNumber))
                .build();

        ContactResDTO adamEmailAddress = createTestEmailAddressResDTO();

        PersonWithContactsResDTO adam = PersonWithContactsResDTO.subBuilder()
                .name("Adam")
                .surname("Kowalski")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999998")
                .contacts(Collections.singletonList(adamEmailAddress))
                .build();

        List<PersonWithContactsResDTO> people = Arrays.asList(adam,jarek);

        PageResDTO<PersonWithContactsResDTO> pageOfPersons = new PageResDTO<>(100,1,people);

        when(personService.readPageOfPersonsWithContacts(0,2,"name","ASC")).thenReturn(pageOfPersons);

        //when
        mvc.perform(get("/api/persons/contacts/page?number=0&size=2&sortedBy=name&order=ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(100)))
                .andExpect(jsonPath("$.currentPageNumber", is(1)))

                .andExpect(jsonPath("$.content[0].name", is(adam.getName())))
                .andExpect(jsonPath("$.content[0].surname", is(adam.getSurname())))
                .andExpect(jsonPath("$.content[0].gender", is(adam.getGender())))
                .andExpect(jsonPath("$.content[0].birthDate", is(adam.getBirthDate())))
                .andExpect(jsonPath("$.content[0].pesel", is(adam.getPesel())))

                .andExpect(jsonPath("$.content[0].contacts[0].value", is(adamEmailAddress.getValue())))
                .andExpect(jsonPath("$.content[0].contacts[0].kind", is(adamEmailAddress.getKind())))

                .andExpect(jsonPath("$.content[1].name", is(jarek.getName())))
                .andExpect(jsonPath("$.content[1].surname", is(jarek.getSurname())))
                .andExpect(jsonPath("$.content[1].gender", is(jarek.getGender())))
                .andExpect(jsonPath("$.content[1].birthDate", is(jarek.getBirthDate())))
                .andExpect(jsonPath("$.content[1].pesel", is(jarek.getPesel())))

                .andExpect(jsonPath("$.content[1].contacts[0].value", is(jarekPhoneNumber.getValue())))
                .andExpect(jsonPath("$.content[1].contacts[0].kind", is(jarekPhoneNumber.getKind())));

        //then
        verify(personService,times(1))
                .readPageOfPersonsWithContacts(0,2,"name","ASC");
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadPersonById_thenIGetPerson() throws Exception{
        //given
        PersonResDTO jarekResDTO = createJarekResDTO();

        when(personService.readPerson(1L)).thenReturn(jarekResDTO);

        //when
        mvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())));
        //then
        verify(personService,times(1))
                .readPerson(1L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadPersonWithContactsById_thenIGetPersonWithContacts() throws Exception{
        //given

        ContactResDTO jarekPhoneNumber = createTestPhoneNumberResDTO();

        ContactResDTO jarekEmailAddress = createTestEmailAddressResDTO();

        PersonWithContactsResDTO jarekResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Arrays.asList(jarekPhoneNumber,jarekEmailAddress))
                .build();

        when(personService.readPersonWithContacts(1L)).thenReturn(jarekResDTO);

        //when
        mvc.perform(get("/api/persons/1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.contacts[0].value", is(jarekPhoneNumber.getValue())))
                .andExpect(jsonPath("$.contacts[0].kind", is(jarekPhoneNumber.getKind())))

                .andExpect(jsonPath("$.contacts[1].value", is(jarekEmailAddress.getValue())))
                .andExpect(jsonPath("$.contacts[1].kind", is(jarekEmailAddress.getKind())));
        //then
        verify(personService,times(1))
                .readPersonWithContacts(1L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIReadPersonWithContactsByPesel_thenIGetPersonWithContacts() throws Exception{
        //given

        ContactResDTO jarekPhoneNumber = createTestPhoneNumberResDTO();

        ContactResDTO jarekEmailAddress = createTestEmailAddressResDTO();

        PersonWithContactsResDTO jarekResDTO = PersonWithContactsResDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate("2016-08-16")
                .pesel("99999999999")
                .contacts(Arrays.asList(jarekPhoneNumber,jarekEmailAddress))
                .build();

        when(personService.readPersonByPeselWithContacts("99999999999")).thenReturn(jarekResDTO);

        //when
        mvc.perform(get("/api/persons/pesel/99999999999/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())))

                .andExpect(jsonPath("$.contacts[0].value", is(jarekPhoneNumber.getValue())))
                .andExpect(jsonPath("$.contacts[0].kind", is(jarekPhoneNumber.getKind())))

                .andExpect(jsonPath("$.contacts[1].value", is(jarekEmailAddress.getValue())))
                .andExpect(jsonPath("$.contacts[1].kind", is(jarekEmailAddress.getKind())));
        //then
        verify(personService,times(1))
                .readPersonByPeselWithContacts("99999999999");
        verifyNoMoreInteractions(personService);
    }


    @Test
    public void whenIReadPersonByPesel_thenIGetPerson() throws Exception{
        //given
        PersonResDTO jarekResDTO = createJarekResDTO();

        when(personService.readPersonByPesel("99999999999")).thenReturn(jarekResDTO);

        //when
        mvc.perform(get("/api/persons/pesel/99999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(jarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(jarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(jarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(jarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(jarekResDTO.getPesel())));
        //then
        verify(personService,times(1))
                .readPersonByPesel("99999999999");
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIUpdatePerson_thenThisPersonIsUpdated() throws Exception{
        //given
        PersonResDTO updatedJarekResDTO = createJarekResDTO();
        UpdatePersonReqDTO updatePersonReqDTO = UpdatePersonReqDTO.subBuilder()
                .id(1L)
                .name("Jarek")
                .surname("Bielec")
                .gender("male")
                .birthDate(LocalDate.parse("2016-08-16"))
                .pesel("99999999999")
                .build();

        when(updatePersonReqDTOValidator.supports(any())).thenReturn(true);
        when(personService.updatePerson(updatePersonReqDTO)).thenReturn(updatedJarekResDTO);

        //when
        mvc.perform(put("/api/persons")
                .content(asJson(updatePersonReqDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(updatedJarekResDTO.getName())))
                .andExpect(jsonPath("$.surname", is(updatedJarekResDTO.getSurname())))
                .andExpect(jsonPath("$.gender", is(updatedJarekResDTO.getGender())))
                .andExpect(jsonPath("$.birthDate", is(updatedJarekResDTO.getBirthDate())))
                .andExpect(jsonPath("$.pesel", is(updatedJarekResDTO.getPesel())));
        //then
        verify(personService,times(1))
                .updatePerson(updatePersonReqDTO);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIDeletePerson_thenThisPersonIsDeleted() throws Exception{
        //when
        mvc.perform(delete("/api/persons/1"))
                .andExpect(status().isOk());
        //then
        verify(personService,times(1))
                .deletePerson(1L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void whenIDeleteAllPersons_thenAllPersonsAreDeleted() throws Exception{
        //when
        mvc.perform(delete("/api/persons"))
                .andExpect(status().isOk());
        //then
        verify(personService,times(1))
                .deleteAllPersons();
        verifyNoMoreInteractions(personService);
    }
}
