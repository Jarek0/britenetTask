package com.britenet.contacts.task.repositories.contact;

import com.britenet.contacts.task.domain.contact.Contact;
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
public interface ContactRepository extends JpaRepository<Contact,Long> {

    @Query("select distinct c from Contact c join fetch c.person where c.id = :id")
    Optional<Contact> findByIdWithPerson(@Param("id") long id);

    @Query("select distinct c from Contact c join fetch c.person")
    List<Contact> findAllWithPersons();

    @Query(
            value = "select distinct c from Contact c join fetch c.person",
            countQuery = "select distinct count(c) from Contact c inner join c.person"
    )
    Page<Contact> findPageWithPersons(Pageable pageable);

}

