package com.sysco.processores;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sysco.common.model.ConfigDataModel;
import com.sysco.dao.CdsLinkScheduleDao;
import com.sysco.entity.CdsLinkSchedule;
import com.sysco.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ExcelTaskletProcessor implements TaskletProcessor {

    @Autowired
    private CdsLinkScheduleDao cdsLinkScheduleDao;


    @Override
    public void process(StepContribution contribution, ChunkContext chunkContext) {

        String fileName = (String)chunkContext.getStepContext().getJobParameters().get("fileName");

        Assert.hasText(fileName, "path name is empty");

        String pathName = FilenameUtils.getPrefix(fileName) + FilenameUtils.getPath(fileName);

        EasyExcel.read(fileName , ConfigDataModel.class, new ReadListener<ConfigDataModel>() {

            private final AtomicInteger index = new AtomicInteger(0);

            public static final int BATCH_COUNT = 300;

            private List<ConfigDataModel> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            private File bakFile = new File(pathName + "bak"+ index.decrementAndGet()+".json");




            @Override
            public void invoke(ConfigDataModel data, AnalysisContext context) {

                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    backAndupdate(cachedDataList);
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }


            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                backAndupdate(cachedDataList);
            }


            private void backAndupdate(List<ConfigDataModel> cachedDataList) {

                List<CdsLinkSchedule> dbData = queryFromDb(cachedDataList);
                backup(dbData);
                HashMap<String, ?>[] prams = convertPrams(cachedDataList, dbData);
                int[] result = cdsLinkScheduleDao.updateBySiteAndCustomer(prams);

                int number = 0;
                for (int i = 0; i < result.length; i++) {
                    if (result[i] == 0) {
                        number++;
                        log.info("failed update's param is {}", prams[i]);
                    }
                }
                log.info("the number of updating successfully is {} and parms = {}", result.length-number, prams);
            }




            private void backup(List<CdsLinkSchedule> dbData) {
                if (CollectionUtils.isEmpty(dbData)) return;
                try {
                    if (Files.exists(bakFile.toPath())) {
                        Files.writeString(bakFile.toPath(), JsonUtils.OBJECT_MAPPER.writeValueAsString(dbData) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                        if (Files.size(bakFile.toPath()) >= 500 * 1024 * 1024) {
                            bakFile = new File(pathName + "bak"+ index.decrementAndGet()+".json");
                        }
                    }else {
                        Files.createFile(bakFile.toPath());
                    }
                } catch (Exception e) {
                    log.error("current batch backup failed, data" + dbData, e);
                }
            }

            private List<CdsLinkSchedule> queryFromDb(List<ConfigDataModel> cachedDataList){
                return cachedDataList.stream().map(model -> {
                    try {
                        CdsLinkSchedule schedule = cdsLinkScheduleDao.queryBySiteAndCustomer(model.getSite(), model.getCustomer());
                        if (Objects.isNull(schedule)) {
                            log.warn("can't query the data form DB for the site = {}, customer = {}", model.getSite(), model.getCustomer());
                        }
                        return schedule;
                    } catch (Exception ex) {
                        log.error("query current data failed, site = "+model.getSite()+", customer = "+ model.getCustomer(), ex);
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            }


            private HashMap<String, ?>[] convertPrams(List<ConfigDataModel> dataList, List<CdsLinkSchedule> dbData) {

                Set<String> keySet = dbData.stream().map(item -> item.getOpcoNumber() + item.getCustomerNumber()).collect(Collectors.toSet());

                List<ConfigDataModel> filterResult = dataList.stream().filter(item -> keySet.contains(item.getSite() + item.getCustomer())).collect(Collectors.toList());

                List<CdsLinkSchedule> result = new ArrayList<>(filterResult.size());

                for (ConfigDataModel dataModel : filterResult) {

                    if (dataModel.getDeliveryDays() == null || !(dataModel.getDeliveryDays().length() == 7)) {
                        log.warn("current config of delivery is incorrect, data = {}", dataModel);
                        continue;
                    }
                    CdsLinkSchedule schedule = new CdsLinkSchedule();
                    schedule.setOpcoNumber(dataModel.getSite());
                    schedule.setCustomerNumber(dataModel.getCustomer());
                    schedule.setDesignatedDelivery(dataModel.getDeliveryDays());
                    if (dataModel.getDeliveryDays().charAt(0) == '0') {
                        schedule.setCutOffSu(dataModel.getCutoff());
                        schedule.setRoutingGroupSu(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(1) == '0') {
                        schedule.setCutOffMo(dataModel.getCutoff());
                        schedule.setRoutingGroupMo(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(2) == '0') {
                        schedule.setCutOffTu(dataModel.getCutoff());
                        schedule.setRoutingGroupTu(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(3) == '0') {
                        schedule.setCutOffWe(dataModel.getCutoff());
                        schedule.setRoutingGroupWe(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(4) == '0') {
                        schedule.setCutOffTh(dataModel.getCutoff());
                        schedule.setRoutingGroupTh(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(5) == '0') {
                        schedule.setCutOffFr(dataModel.getCutoff());
                        schedule.setRoutingGroupFr(dataModel.getRoutingCode());
                    }
                    if (dataModel.getDeliveryDays().charAt(6) == '0') {
                        schedule.setCutOffSa(dataModel.getCutoff());
                        schedule.setRoutingGroupSa(dataModel.getRoutingCode());
                    }

                    result.add(schedule);
                }

                List<HashMap<String, Object>> collect = result.stream().map(item -> JsonUtils.OBJECT_MAPPER
                        .convertValue(item, new TypeReference<HashMap<String, Object>>() {})).collect(Collectors.toList());


                return collect.toArray(new HashMap[]{});
            }



        }).headRowNumber(1).autoTrim(false).doReadAll();


    }
}
