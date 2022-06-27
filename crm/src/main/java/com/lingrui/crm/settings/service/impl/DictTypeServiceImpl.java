package com.lingrui.crm.settings.service.impl;

import com.lingrui.crm.settings.domain.DictType;
import com.lingrui.crm.settings.mapper.DictTypeMapper;
import com.lingrui.crm.settings.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 23:23
 * @ Version 1.0
 */
@Service("dictTypeService")
public class DictTypeServiceImpl implements DictTypeService {
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    public List<DictType> queryAllDictType() {
        return dictTypeMapper.selectAllDictType();
    }

    @Override
    public int saveCreateDictType(DictType dictType) {
        return dictTypeMapper.insertDictType(dictType);
    }

    @Override
    public int saveEditDictType(DictType dictType) {
        return dictTypeMapper.updateDictType(dictType);
    }

    @Override
    public int deleteDictTypeByCode(String[] codes) {
        return dictTypeMapper.deleteDictTypeByCodes(codes);
    }

    @Override
    public DictType queryDictTypeByCode(String code) {
        return dictTypeMapper.selectDictTypeByCode(code);
    }
}
