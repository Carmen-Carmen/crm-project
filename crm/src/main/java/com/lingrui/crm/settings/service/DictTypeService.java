package com.lingrui.crm.settings.service;

import com.lingrui.crm.settings.domain.DictType;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 23:23
 * @ Version 1.0
 */
public interface DictTypeService {
    List<DictType> queryAllDictType();

    int saveCreateDictType(DictType dictType);

    int saveEditDictType(DictType dictType);

    int deleteDictTypeByCode(String[] codes);

    DictType queryDictTypeByCode(String code);
}
