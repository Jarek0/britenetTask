package com.britenet.contacts.task.repositories.contact;

import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber,Long> {

    @Query("select p from PhoneNumber p join p.person p where p.id = :id")
    Optional<PhoneNumber> findPhoneNumberByIdWithPerson(long id);

    Optional<PhoneNumber> findByValue(String value);
}
