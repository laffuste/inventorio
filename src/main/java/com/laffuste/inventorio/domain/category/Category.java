package com.laffuste.inventorio.domain.category;

import lombok.*;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // hide constructor, force usage of Builder
@With
public class Category {

    Long id;
    String name;
    Long parentId;

}
