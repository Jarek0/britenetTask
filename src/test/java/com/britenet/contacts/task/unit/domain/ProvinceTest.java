package com.britenet.contacts.task.unit.domain;

import com.britenet.contacts.task.domain.contact.subClasses.enums.Province;
import com.britenet.contacts.task.exceptions.invalidInput.enumType.InvalidProvinceNameException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProvinceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void tryToCreateInvalidProvince(){
        //then
        expectedException.expect(InvalidProvinceNameException.class);
        expectedException.expectMessage("Invalid province name");

        //when
        Province.getByName("invalid");
    }
}
