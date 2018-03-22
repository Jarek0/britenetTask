package com.britenet.contacts.task.mappers.page;

import com.britenet.contacts.task.DTO.page.PageResDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PageMapperImpl implements PageMapper{
    @Override
    public <DTO, T> PageResDTO<DTO> mapToResDTO(Page<T> page, Function<T, DTO> mapContentFunction) {
        return new PageResDTO<>(
                page.getTotalPages(),
                page.getNumber(),
                page.getContent().stream().map(mapContentFunction).collect(Collectors.toList()));
    }
}
