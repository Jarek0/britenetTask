package com.britenet.contacts.task.repositories.contact;

import com.britenet.contacts.task.domain.contact.subClasses.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{

    @Query("select a from Address a left join fetch a.person p where a.id = :id")
    Optional<Address> findAddressByIdWithPerson(@Param("id") long id);

}
