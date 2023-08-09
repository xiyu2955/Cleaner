package com.sysco;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.sysco.common.model.ConfigDataModel;

import java.io.FileNotFoundException;
import java.util.List;

public class MainTest {

    public static void main(String[] args) throws FileNotFoundException {

        // 输入输出文件路径
        String src = "D:\\data\\CDSM_Data_review.xlsx";
        String dest = "D:\\data\\data";

        // 预计每个sheet的行数
        int rowsPerSheet = 10000;


        EasyExcel.read(src, ConfigDataModel.class, new ReadListener<ConfigDataModel>() {

            int index = 0;
            List<ConfigDataModel> sheetList = ListUtils.newArrayListWithCapacity(rowsPerSheet);


            @Override
            public void invoke(ConfigDataModel data, AnalysisContext context) {

                sheetList.add(data);
                if (sheetList.size() % rowsPerSheet == 0) {
                    EasyExcel.write(dest + index + ".xlsx").sheet("Sheet1").head(ConfigDataModel.class).doWrite(sheetList);
                    index++;
                    sheetList = ListUtils.newArrayListWithCapacity(rowsPerSheet);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                EasyExcel.write(dest + index + ".xlsx").sheet("Sheet1").head(ConfigDataModel.class).doWrite(sheetList);
            }
        }).headRowNumber(2).autoTrim(false).doReadAll();

    }
}
