package com.britenet.contacts.task.DTO.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResDTO<T> {
    private int totalPages;
    private int currentPageNumber;
    private List<T> content;
}
