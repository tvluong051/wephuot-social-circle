package com.lightdevel.wephuot.socialcircle.models.out;

import lombok.Data;

@Data
public class SearchResult<T> {
    private final Integer total;
    private final T results;
}
