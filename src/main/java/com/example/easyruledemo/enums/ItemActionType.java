package com.example.easyruledemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-06-25 13:50
 */
@Getter
@AllArgsConstructor
public enum ItemActionType {

    D("D","'D'","附件下载"),
    DC("DC","'DC'","附件下载并拷贝");

    private String code;
    private String key;
    private String description;

    public static ItemActionType getEnum(String itemActionCode) {
        ItemActionType[] enumValues = ItemActionType.values();
        for (ItemActionType itemAction:enumValues) {
            if (itemAction.getCode().equals(itemActionCode.trim())) {
                return itemAction;
            }
        }
        throw new RuntimeException("获取itemActionType异常");
//        return null;
    }

    public static List<ItemActionType> getEnumList(List<String> itemActionCodeList){
        List<ItemActionType> itemActionTypeEnumList = new ArrayList<>();
        for(String itemActionCode:itemActionCodeList){
            ItemActionType[] enumValues = ItemActionType.values();
            for (ItemActionType itemAction:enumValues) {
                if (itemAction.getCode().equals(itemActionCode.trim())) {
                    itemActionTypeEnumList.add(itemAction);
                }
            }
//            throw new RuntimeException("获取itemActionType异常");
        }
        return itemActionTypeEnumList;
    }

}
