package com.sysco.common.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Report<T> {
    private Integer totalNumberOfNeedUpdate = 0;
    private Integer totalNumberOfUpdateFailed = 0;
    private List<T> itemsOfUpdateFailed = Collections.emptyList();
}
