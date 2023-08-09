package com.sysco.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CdsLinkSchedule {
    private Integer id;
    private Integer cdsId;
    private String designatedDelivery;
    private String storeOpenTime;
    private String storeCloseTime;
    private String timeWindow1Start;
    private String timeWindow1Stop;
    private String timeWindow2Start;
    private String timeWindow2Stop;
    private String cutOffSu;
    private String cutOffMo;
    private String cutOffTu;
    private String cutOffWe;
    private String cutOffTh;
    private String cutOffFr;
    private String cutOffSa;
    private String cutoffOverrideExternal;
    private String cutoffOverrideInternal;
    private String sendDataStatus;
    private LocalDateTime createTimestamp;
    private LocalDateTime modifiedTimestamp;
    private String modifiedBy;
    private String syncToAs400Status;
    private String requestId;
    private Date startDate;
    private Date endDate;
    private Integer syncToAs400RetryTimes;
    private Boolean perksInd;
    private Boolean sywInd;
    private String sywNeighbourhood;
    private Integer leadTime;
    private String customerSegmentationScore;
    private String manifestNotes;
    private String timeWindowType;
    private String routingGroupSu;
    private String routingGroupMo;
    private String routingGroupTu;
    private String routingGroupWe;
    private String routingGroupTh;
    private String routingGroupFr;
    private String routingGroupSa;
    private String opcoNumber;
    private String customerNumber;
}
