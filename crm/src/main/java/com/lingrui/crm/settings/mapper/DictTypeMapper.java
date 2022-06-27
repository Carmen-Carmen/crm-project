package com.lingrui.crm.settings.mapper;

import com.lingrui.crm.settings.domain.DictType;

import java.util.List;

public interface DictTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    int insert(DictType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    int insertSelective(DictType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    DictType selectByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    int updateByPrimaryKeySelective(DictType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbg.generated Sat Jun 18 20:49:33 CST 2022
     */
    int updateByPrimaryKey(DictType record);

    /**
     * @param :
     * @return List<DictType>
     * @author xulingrui
     * @description TODO
     * 获取所有字典类型
     * @date 2022/6/18 23:14
     */
    List<DictType> selectAllDictType();

    /**
     * @param dictType:
     * @return int
     * @author xulingrui
     * @description TODO
     * 添加一条字典类型
     * @date 2022/6/19 00:12
     */
    int insertDictType(DictType dictType);

    /**
     * @param dictType:
     * @return int
     * @author xulingrui
     * @description TODO
     * 更新编辑过的字典类型信息
     * @date 2022/6/19 00:14
     */
    int updateDictType(DictType dictType);

    /**
     * @param codes:
     * @return int
     * @author xulingrui
     * @description TODO
     * 根据包含多个code的字符串数组删除字典类型
     * @date 2022/6/20 08:44
     */
    int deleteDictTypeByCodes(String[] codes);

    /**
     * @param code:
     * @return DictType
     * @author xulingrui
     * @description TODO
     * 根据编码查询字典类型
     * @date 2022/6/20 10:15
     */
    DictType selectDictTypeByCode(String code);
}