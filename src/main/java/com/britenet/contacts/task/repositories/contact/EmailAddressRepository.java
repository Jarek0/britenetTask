package com.britenet.contacts.task.repositories.contact;

import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public interface EmailAddressRepository extends JpaRepository<EmailAddress,Long> {

    @Query("select e from EmailAddress e left join fetch e.person p where e.id = :id")
    Optional<EmailAddress> findEmailAddressByIdWithPerson(@Param("id")long id);

    Optional<EmailAddress> findByValue(@Param("value") String value);
}
