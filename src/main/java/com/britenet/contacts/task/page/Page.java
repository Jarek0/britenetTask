package com.britenet.contacts.task.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    private int totalPages;
    private int currentPageNumber;
    private String sortedBy;
    private PageOrder orderBy;
    private List<T> content;
}
