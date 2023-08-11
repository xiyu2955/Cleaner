package com.sysco.dao.impl;

import com.sysco.dao.CdsLinkScheduleDao;
import com.sysco.entity.CdsLinkSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Component
public class CdsLinkScheduleDaoImpl implements CdsLinkScheduleDao {

    public static String QUERY_BY_SITE_AND_CUSTOMER = "select cls.id,cls.cds_id,cls.designated_delivery,cls.store_open_time,cls.store_close_time,cls.time_window_1_start,cls.time_window_1_stop,cls.time_window_2_start,cls.time_window_2_stop,cls.cut_off_su,cls.cut_off_mo,cls.cut_off_tu,cls.cut_off_we,cls.cut_off_th,cls.cut_off_fr,cls.cut_off_sa,cls.cutoff_override_external,cls.cutoff_override_internal,cls.send_data_status,cls.create_timestamp,cls.modified_timestamp,cls.modified_by,cls.sync_to_as400_status,cls.request_id,cls.start_date,cls.end_date,cls.sync_to_as400_retry_times,cls.perks_ind,cls.syw_ind,cls.syw_neighbourhood,cls.lead_time,cls.customer_segmentation_score,cls.manifest_notes,cls.time_window_type,cls.routing_group_su,cls.routing_group_mo,cls.routing_group_tu,cls.routing_group_we,cls.routing_group_th,cls.routing_group_fr,cls.routing_group_sa,cds.opco_number,cds.customer_number " +
            "from customer_delivery_schedule cds join cds_link_schedule cls  on cds.id = cls.cds_id " +
            "where cds.opco_number = :site " +
            "and cds.customer_number = :customer";

    public static String UPDATE_BY_SITE_AND_CUSTOMER = "UPDATE cds_link_schedule " +
            "SET designated_delivery=:designatedDelivery, cut_off_su=:cutOffSu, cut_off_mo=:cutOffMo, cut_off_tu=:cutOffTu, cut_off_we=:cutOffWe, cut_off_th=:cutOffTh, cut_off_fr=:cutOffFr, cut_off_sa=:cutOffSa, send_data_status=:sendDataStatus,sync_to_as400_status=:syncToAs400Status,routing_group_su=:routingGroupSu, routing_group_mo=:routingGroupMo, routing_group_tu=:routingGroupTu, routing_group_we=:routingGroupWe, routing_group_th=:routingGroupTh, routing_group_fr=:routingGroupFr, routing_group_sa=:routingGroupSa " +
            "from customer_delivery_schedule where customer_delivery_schedule.id = cds_link_schedule.cds_id " +
            "and customer_delivery_schedule.opco_number =:opcoNumber " +
            "and customer_delivery_schedule.customer_number =:customerNumber";


    @Autowired
    private NamedParameterJdbcOperations jdbcTemplate;


    @Override
    public CdsLinkSchedule queryBySiteAndCustomer(String site, String customer) {

        List<CdsLinkSchedule> linkScheduleList = jdbcTemplate.query(QUERY_BY_SITE_AND_CUSTOMER, Map.of("site", site, "customer", customer),
                new BeanPropertyRowMapper<>(CdsLinkSchedule.class) {});


        Assert.isTrue(linkScheduleList.size() <= 1, "expected is less than or eaqules 1, actuality granter than 1, site = " + site + ", customer = " + customer);


        return CollectionUtils.isEmpty(linkScheduleList) ? null : linkScheduleList.get(0);
    }

    @Override
    public int[] updateBySiteAndCustomer(Map<String, ?>[] prams) {

        return jdbcTemplate.batchUpdate(UPDATE_BY_SITE_AND_CUSTOMER, prams);
    }




}
