package com.britenet.contacts.task.page;

import com.britenet.contacts.task.exceptions.invalidInput.InvalidOrderTypeException;
import com.britenet.contacts.task.exceptions.invalidInput.InvalidSortTypeException;
import lombok.Data;
import org.assertj.core.util.Preconditions;

@Data
public class PageRequest<T>{
    private Class<T> type;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private PageOrder orderBy;

    public PageRequest(Class<T> type,int pageNumber, int pageSize, String sortedBy,String orderBy){
        this.type = type;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortedBy = checkAndReturnSortedBy(sortedBy);
        this.orderBy = checkAndReturnOrderBy(orderBy);
    }

    private String checkAndReturnSortedBy(String sortedBy){
        try {
            Preconditions.checkNotNull(sortedBy);
            type.getField(sortedBy);
        }
        catch (NoSuchFieldException e){
            throw new InvalidSortTypeException();
        }
        return sortedBy;
    }

    private PageOrder checkAndReturnOrderBy(String orderBy){
        if(orderBy == null || !orderBy.equals("ASC") && !orderBy.equals("DESC")){
            throw new InvalidOrderTypeException();
        }
        return PageOrder.valueOf(orderBy);
    }
}
