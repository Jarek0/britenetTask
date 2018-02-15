package com.britenet.contacts.task.DAO.contact;

import com.britenet.contacts.task.domain.contact.Contact;
import com.britenet.contacts.task.DAO.AbstractDAO;
import com.britenet.contacts.task.page.Page;
import com.britenet.contacts.task.page.PageRequest;
import com.britenet.contacts.task.domain.contact.subClasses.Address;
import com.britenet.contacts.task.domain.contact.subClasses.EmailAddress;
import com.britenet.contacts.task.domain.contact.subClasses.PhoneNumber;
import javassist.tools.rmi.ObjectNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Transactional(
        rollbackFor = {ObjectNotFoundException.class},
        isolation = Isolation.READ_COMMITTED)
@Repository
@SuppressWarnings("unchecked")
public class ContactDAO extends AbstractDAO<Contact,Long> {

    @Autowired
    public ContactDAO(EntityManagerFactory factory) {
        super(Contact.class,factory);
    }

    public List<Contact> findAllWithPersons() {
        Query query = currentSession.createQuery("from Contact c join fetch c.person p");
        return (List<Contact>) query.list();
    }

    public Optional<Contact> findByIdWithPerson(long contactId) {
        Query query = currentSession.createQuery("from Contact c join fetch c.person p where c.id = :id")
                .setParameter("id",contactId);
        return Optional.ofNullable((Contact) query.uniqueResult());
    }

    public Page<Contact> findPageWithPersons(PageRequest<Contact> pageRequest) {
        Criteria pageWithContactsQuery = currentSession.createCriteria(Contact.class)
                .setFetchMode("person", FetchMode.JOIN);
        return executePageQueryPage(pageRequest,pageWithContactsQuery);
    }

    public Optional<Address> findAddressByIdWithPerson(long contactId){
        Query query = currentSession.createQuery("from Address a join fetch a.person p where a.id = :id")
                .setParameter("id",contactId);
        return Optional.ofNullable((Address) query.uniqueResult());
    }

    public Optional<EmailAddress> findEmailAddressByIdWithPerson(long contactId){
        Query query = currentSession.createQuery("from EmailAddress e join fetch e.person p where e.id = :id")
                .setParameter("id",contactId);
        return Optional.ofNullable((EmailAddress) query.uniqueResult());
    }

    public Optional<PhoneNumber> findPhoneNumberByIdWithPerson(long contactId){
        Query query = currentSession.createQuery("from PhoneNumber c join fetch c.person p where c.id = :id")
                .setParameter("id",contactId);
        return Optional.ofNullable((PhoneNumber) query.uniqueResult());
    }
}
