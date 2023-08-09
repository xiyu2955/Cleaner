package com.sysco.dao;

import com.sysco.entity.CdsLinkSchedule;

import java.util.Map;

public interface CdsLinkScheduleDao {
    CdsLinkSchedule queryBySiteAndCustomer(String site, String customer);

    int[] updateBySiteAndCustomer(Map<String, ?>[] prams);
}
