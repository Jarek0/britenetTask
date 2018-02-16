package com.britenet.contacts.task.repository.person;

import com.britenet.contacts.task.domain.person.Person;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public interface PersonRepository extends JpaRepository<Person,Long>{

    @Query("select p from Person p join p.contacts c")
    List<Person> findAllWithContacts();

    @Query("select p from Person p join p.contacts c")
    Page<Person> findPageWithContacts(Pageable pageable);

    @Query("select p from Person p where p.id = :id")
    Optional<Person> findById(long id);

    Optional<Person> findByPesel(String pesel);

    @Query("select p from Person p join p.contacts c where p.id = :id")
    Optional<Person> findByIdWithContacts(long id);

    @Query("select p from Person p join p.contacts c where p.id = :id")
    Optional<Person> findByPeselWithContacts(String pesel);
}
