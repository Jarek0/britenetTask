package com.britenet.contacts.task.mappers.page;


import com.britenet.contacts.task.DTO.page.PageResDTO;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public interface PageMapper {
    <DTO,T> PageResDTO<DTO> mapToResDTO(Page<T> page, Function<T, DTO> mapContentFunction);
}
