package com.sysco.common.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.sysco.converters.TrimConverter;
import lombok.Data;

@Data
public class ConfigDataModel {
    @ExcelProperty(index = 0, converter = TrimConverter.class)
    private String site;
    @ExcelProperty(index = 1, converter = TrimConverter.class)
    private String customer;
    @ExcelProperty(index = 2)
    private String deliveryDays;
    @ExcelProperty(index = 16, converter = TrimConverter.class)
    private String routingCode;
    @ExcelProperty(index = 17, converter = TrimConverter.class)
    private String cutoff;


    /*@ExcelProperty({"Site"})
    private String site;
    @ExcelProperty("Ship To")
    private String customer;
    @ExcelProperty({"SUS value"})
    private String deliveryDays;
    @ExcelProperty("Default Code")
    private String routingCode;
    @ExcelProperty("Default Time")
    private String cutoff;*/

}
