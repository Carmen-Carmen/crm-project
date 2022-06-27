package com.lingrui.crm.settings.service.impl;

import com.lingrui.crm.settings.domain.DictValue;
import com.lingrui.crm.settings.mapper.DictValueMapper;
import com.lingrui.crm.settings.service.DictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 23:19
 * @ Version 1.0
 */
@Service("dictValueService")
public class DictValueServiceImpl implements DictValueService {
    @Autowired
    private DictValueMapper dictValueMapper;

    @Override
    public List<DictValue> queryAllDictValue() {
        return dictValueMapper.selectAllDictValue();
    }

    @Override
    public int saveCreateDictValue(DictValue dictValue) {
        return dictValueMapper.insertDictValue(dictValue);
    }

    @Override
    public int saveEditDictValue(DictValue dictValue) {
        return dictValueMapper.updateDictValue(dictValue);
    }

    @Override
    public int deleteDictValueByIds(String[] ids) {
        return dictValueMapper.deleteDictValueByIds(ids);
    }

    @Override
    public DictValue queryDictValueById(String id) {
        return dictValueMapper.selectDictValueById(id);
    }

    @Override
    public List<DictValue> queryDictValueByTypeCode(String typeCode) {
        return dictValueMapper.selectDictValueByTypeCode(typeCode);
    }
}
