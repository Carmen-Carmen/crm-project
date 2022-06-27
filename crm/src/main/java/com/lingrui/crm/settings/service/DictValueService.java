package com.lingrui.crm.settings.service;

import com.lingrui.crm.settings.domain.DictValue;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 23:18
 * @ Version 1.0
 */
public interface DictValueService {
    List<DictValue> queryAllDictValue();

    int saveCreateDictValue(DictValue dictValue);

    int saveEditDictValue(DictValue dictValue);

    int deleteDictValueByIds(String[] ids);

    DictValue queryDictValueById(String id);

    List<DictValue> queryDictValueByTypeCode(String typeCode);
}
