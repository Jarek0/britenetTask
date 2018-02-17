package com.britenet.contacts.task.repositories.person;

import com.britenet.contacts.task.domain.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public interface PersonRepository extends JpaRepository<Person,Long>{

    @Query("select p from Person p where p.id = :id")
    Optional<Person> findById(@Param("id") long id);

    Optional<Person> findByPesel(String pesel);

    @Query("select distinct p from Person p join fetch p.contacts c where p.id = :id")
    Optional<Person> findByIdWithContacts(@Param("id") long id);

    @Query("select distinct p from Person p join fetch p.contacts c where p.pesel = :pesel")
    Optional<Person> findByPeselWithContacts(@Param("pesel") String pesel);

    @Query("select distinct p from Person p join fetch p.contacts c")
    List<Person> findAllWithContacts();

    @Query(
            value = "select distinct p from Person p join fetch p.contacts c",
            countQuery = "select distinct count(p) from Person p inner join p.contacts c"
    )
    Page<Person> findPageWithContacts(Pageable pageable);
}
