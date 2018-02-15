package com.britenet.contacts.task.DAO.person;

import com.britenet.contacts.task.DAO.AbstractDAO;
import com.britenet.contacts.task.page.Page;
import com.britenet.contacts.task.page.PageRequest;
import com.britenet.contacts.task.domain.person.Person;
import javassist.tools.rmi.ObjectNotFoundException;
import org.assertj.core.util.Preconditions;
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
public class PersonDAO extends AbstractDAO<Person,Long> {

    @Autowired
    public PersonDAO(EntityManagerFactory factory) {
        super(Person.class,factory);
    }

    public boolean personWithThisPeselExist(String pesel){
        Query query = currentSession.createQuery("select 1 from Person p where p.pesel = :pesel")
                .setParameter("pesel",pesel);

        return query.uniqueResult() != null;
    }

    public List<Person> findAllWithContacts() {
        Query query = currentSession.createQuery("from Person p join fetch p.contacts c");
        return (List<Person>) query.list();
    }

    public Page<Person> findPageWithContacts(PageRequest pageRequest) {
        Criteria pageWithContactsQuery = currentSession.createCriteria(Person.class)
                .setFetchMode("contacts", FetchMode.JOIN);
        return executePageQueryPage(pageRequest,pageWithContactsQuery);
    }

    public Optional<Person> findByPesel(String pesel) {
        Preconditions.checkNotNull(pesel);
        Query query = currentSession.createQuery("from Person p where p.pesel = :pesel")
                .setParameter("pesel",pesel);
        return Optional.ofNullable((Person) query.uniqueResult());
    }

    public Optional<Person> findByIdWithContacts(long id){
        Query query = currentSession.createQuery("from Person p join fetch p.contacts c where p.id = :id")
                .setParameter("id",id);
        return Optional.ofNullable((Person) query.uniqueResult());
    }

    public Optional<Person>  findByPeselWithContacts(String pesel) {
        Query query = currentSession.createQuery("from Person p join fetch p.contacts c where p.pesel = :pesel")
                .setParameter("pesel",pesel);
        return Optional.ofNullable((Person) query.uniqueResult());
    }
}
