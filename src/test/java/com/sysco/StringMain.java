package com.sysco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysco.entity.CdsLinkSchedule;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringMain {
    public static void main(String[] args) {

        System.out.println("   0   ".length());

    }



    public static void covert() {
        String s = "id, cds_id, designated_delivery, store_open_time, store_close_time, time_window_1_start, time_window_1_stop, time_window_2_start, time_window_2_stop, cut_off_su, cut_off_mo, cut_off_tu, cut_off_we, cut_off_th, cut_off_fr, cut_off_sa, cutoff_override_external, cutoff_override_internal, send_data_status, create_timestamp, modified_timestamp, modified_by, sync_to_as400_status, request_id, start_date, end_date, sync_to_as400_retry_times, perks_ind, syw_ind, syw_neighbourhood, lead_time, customer_segmentation_score, manifest_notes, time_window_type, routing_group_su, routing_group_mo, routing_group_tu, routing_group_we, routing_group_th, routing_group_fr, routing_group_sa";


        String[] split = s.split(",");

        System.out.println(Stream.of(split).map(item -> "cls." + item.trim()).collect(Collectors.joining(",")));
    }


}
