package com.sysco;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sysco.entity.CdsLinkSchedule;
import com.sysco.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BackupTest {
    public static void main(String[] args) throws IOException {

        String path = "C:\\Users\\ARMIN.YU\\Downloads\\bak-1.json";
        File backup = new File(path);
        List<String> jsonList = Files.readAllLines(backup.toPath());

        List<Integer> idList = jsonList.stream().map(json -> JsonUtils.fromJson(json, new TypeReference<ArrayList<CdsLinkSchedule>>() {
        })).flatMap(Collection::stream).map(CdsLinkSchedule::getId).collect(Collectors.toList());

        List<List<Integer>> collect = new ArrayList<>(idList.stream()
                .collect(Collectors.groupingBy(id -> idList.indexOf(id) / 100)).values());


        System.out.println(collect);


    }
}
