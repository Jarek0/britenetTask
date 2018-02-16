package com.britenet.contacts.task.repository.contact;

import com.britenet.contacts.task.domain.contact.Contact;
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
public interface ContactRepository extends JpaRepository<Contact,Long> {

    @Query("select c from Contact c join c.person p")
    List<Contact> findAllWithPersons();

    @Query("select c from Contact c join c.person p where c.id = :id")
    Optional<Contact> findByIdWithPerson(long id);

    @Query("select c from Contact c join c.person p")
    Page<Contact> findPageWithPersons(Pageable pageable);

}

