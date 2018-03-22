package com.britenet.contacts.task.integration.controllers;

import com.britenet.contacts.task.DTO.person.request.PersonReqDTO;
import com.britenet.contacts.task.DTO.person.response.PersonResDTO;
import com.britenet.contacts.task.TaskApplication;
import com.britenet.contacts.task.repositories.contact.ContactRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarekReqDTO;
import static com.britenet.contacts.task.testObjectsFactories.TestPersonFactory.createJarekResDTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TaskApplication.class}, properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class PersonControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void whenICreatePersonAndGetListOfPeople_thenIThisPersonIsOnTheList() throws Exception{//EXAMPLE
        //given

        PersonReqDTO jarekReqDTO = createJarekReqDTO();

        HttpEntity<PersonReqDTO> jarekRequestEntity = prepareRequestEntity(jarekReqDTO);

        //when

        ResponseEntity responsePersonEntity = restTemplate.postForEntity(this.createURLWithPort("/api/persons"),jarekRequestEntity, String.class);
        ResponseEntity<String> responsePersonsListEntity = restTemplate.getForEntity(this.createURLWithPort("/api/persons"), String.class);

        //then
        assertEquals(HttpStatus.OK, responsePersonEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responsePersonsListEntity.getStatusCode());

        Gson gson = new Gson();

        PersonResDTO expectedResDTO = createJarekResDTO();

        assertTrue(responsePersonsListEntity.getBody().contains(gson.toJson(expectedResDTO)));
    }

    static public HttpEntity<PersonReqDTO> prepareRequestEntity(PersonReqDTO req){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(req,headers);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @After
    public void clearDatabase(){
        contactRepository.deleteAll();
    }
}
