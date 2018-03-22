package com.britenet.contacts.task.unit.domain;

import com.britenet.contacts.task.domain.person.enums.Gender;
import com.britenet.contacts.task.exceptions.invalidInput.enumType.InvalidGenderKindException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void tryToCreateInvalidGender(){
        //then
        expectedException.expect(InvalidGenderKindException.class);
        expectedException.expectMessage("Invalid gender kind");

        //when
        Gender.getByKind("man");
    }
}
