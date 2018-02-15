package com.britenet.contacts.task.DAO;

import com.britenet.contacts.task.exceptions.notFound.ObjectNotFoundException;
import com.britenet.contacts.task.page.Page;
import com.britenet.contacts.task.page.PageOrder;
import com.britenet.contacts.task.page.PageRequest;
import org.assertj.core.util.Preconditions;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional(
        rollbackFor = {ObjectNotFoundException.class},
        isolation = Isolation.READ_COMMITTED)
@SuppressWarnings("unchecked")
public abstract class AbstractDAO<T, Id extends Serializable> implements DAO<T, Id>{

    protected Session currentSession;

    private SessionFactory sessionFactory;

    private final Class<T> type;

    protected AbstractDAO(Class<T> type, EntityManagerFactory factory) {
        this.type = type;
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }
    
    //Session

    public void openSession(){
        currentSession = sessionFactory.openSession();
    }

    public void closeSession(){
        currentSession.close();
    }

    //CRUD

    public Optional<T> findById(Id id){
        Preconditions.checkNotNull(id);
        T foundObject = currentSession.get(type,id);

        return Optional.ofNullable(foundObject);
    }

    public List<T> findAll(){
        return currentSession.createCriteria(type).list();
    }

    public Page<T> findPage(PageRequest pageRequest){
        Criteria pageQuery = currentSession.createCriteria(type);
        return executePageQueryPage(pageRequest,pageQuery);
    }

    public Optional<T> save(T objectToSave){
        return Optional.ofNullable((T) currentSession.save(objectToSave));
    }

    public Optional<T> edit(T objectToEdit){
        return Optional.ofNullable((T) currentSession.merge(objectToEdit));
    }

    public void delete(T objectToDelete){
        currentSession.delete(objectToDelete);
    }

    public void deleteById(Id id){
        Preconditions.checkNotNull(id);
        T objectToDelete = Optional.ofNullable(currentSession.get(type,id))
                .orElseThrow(() -> new ObjectNotFoundException(type));
        currentSession.delete(objectToDelete);
    }

    public void deleteAll(){
        findAll().forEach(this::delete);
    }

    //Page methods

    protected Page<T> executePageQueryPage(PageRequest<T> pageRequest, Criteria pageQuery){
        int totalElements = totalElements();

        int requestedPageSize = pageRequest.getPageSize();
        int requestedPageNumber = pageRequest.getPageNumber();
        String sortedBy = pageRequest.getSortedBy();
        PageOrder orderBy = pageRequest.getOrderBy();

        int possiblePageSize = possiblePageSize(requestedPageSize,totalElements);
        int possiblePageNumber = possiblePageNumber(requestedPageNumber,possiblePageSize,totalElements);
        int firstResultNumber = firstResultNumber(possiblePageNumber,possiblePageSize);

        List<T> results = pageQuery
                .setFirstResult(firstResultNumber)
                .setMaxResults(possiblePageSize)
                .addOrder(orderBy == PageOrder.ASC ? Order.asc(sortedBy) : Order.desc(sortedBy))
                .list();

        int totalPagesWithRequestedSize = totalElements/requestedPageSize;

        return new Page<>(
                totalPagesWithRequestedSize,
                possiblePageNumber,
                sortedBy,
                orderBy,
                results);
    }

    private int totalElements(){
        return (int) currentSession.createCriteria(type).uniqueResult();
    }

    private int possiblePageSize(int requestedPageSize, int totalElements){
        int possiblePageSize = requestedPageSize;
        if (possiblePageSize > totalElements) {
            possiblePageSize = totalElements;
        }
        return possiblePageSize;
    }

    private int possiblePageNumber(int requestedPageNumber,int possiblePageSize, int totalElements){
        int possiblePageNumber = requestedPageNumber;
        if (possiblePageNumber > totalElements / possiblePageSize) {
            possiblePageNumber = totalElements / possiblePageSize;
        }
        return possiblePageNumber;
    }

    private int firstResultNumber(int possiblePageNumber, int possiblePageSize){
        return possiblePageNumber*possiblePageSize;
    }

}
