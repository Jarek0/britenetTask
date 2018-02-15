package com.britenet.contacts.task.DAO;


import com.britenet.contacts.task.page.Page;
import com.britenet.contacts.task.page.PageRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface DAO<T, Id extends Serializable>{

    //Session

    void openSession();

    void closeSession();

    //CRUD

    Optional<T> findById(Id id);

    List<T> findAll();

    Page<T> findPage(PageRequest pageRequest);

    Optional<T> save(T objectToSave);

    Optional<T> edit(T objectToEdit);

    void delete(T objectToDelete);

    void deleteById(Id id);

    void deleteAll();

}
