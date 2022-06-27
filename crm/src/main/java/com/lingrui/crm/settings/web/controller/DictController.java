package com.lingrui.crm.settings.web.controller;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.settings.domain.DictType;
import com.lingrui.crm.settings.domain.DictValue;
import com.lingrui.crm.settings.service.DictTypeService;
import com.lingrui.crm.settings.service.DictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-06-18 21:16
 * @ Version 1.0
 */
@Controller
public class DictController {
    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private DictValueService dictValueService;

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 跳转到数据字典主页的控制器方法
     * @date 2022/6/18 21:24
     */
    @RequestMapping("/settings/dictionary/index.do")
    public String index() {
        return "settings/dictionary/index";
    }

    //*************************************DictType相关********************************************

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 显示字典类型主页
     * @date 2022/6/18 21:24
     */
    @RequestMapping("/settings/dictionary/type/typeIndex.do")
    public String typeIndex(HttpServletRequest request) {
        //调用DictTypeService查询所有数据
        List<DictType> dictTypeList = dictTypeService.queryAllDictType();
        //放入request作用域
        request.setAttribute("dictTypeList", dictTypeList);
        //服务器内部转发跳转页面
        return "settings/dictionary/type/index";
    }

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 控制跳转到新建字典类型的页面
     * @date 2022/6/19 23:00
     */
    @RequestMapping("/settings/dictionary/type/toCreateDictType.do")
    public String toCreateDictType() {
        return "settings/dictionary/type/save";
    }

    /**
     * @param dictType:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存新建的字典类型
     * @date 2022/6/20 11:16
     */
    @RequestMapping("/settings/dictionary/type/saveCreateDictType.do")
    @ResponseBody
    public Object saveCreateDictType(DictType dictType) {
        ObjectForReturn objectForReturn = null;
        try {
            //1、调用service层，完成保存新增字典类型的
            int affectedRows = dictTypeService.saveCreateDictType(dictType);

            //2、根据结果，生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param code:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 根据前台传来的code=xXX&code=yYY...code=zZZ，springMVC自动封装成变量名为code的字符串数组
     * 删除code为这些的字典类型
     * @date 2022/6/20 11:17
     */
    @RequestMapping("/settings/dictionary/type/deleteDictType.do")
    @ResponseBody
    public Object deleteDictType(String[] code) {
        ObjectForReturn objectForReturn = null;
        try {
            //1、调用service层，完成删除字典类型操作
            int affectedRows = dictTypeService.deleteDictTypeByCode(code);

            //2、根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == code.length) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param code:
     * @param request:
     * @return String
     * @author xulingrui
     * @description TODO
     * 跳转到编辑字典类型的方法
     * 同时将要回显的内容放在requestScope
     * @date 2022/6/20 10:14
     */
    @RequestMapping("/settings/dictionary/type/toEditDictType.do")
    public String toEditDictType(String code, HttpServletRequest request) {
        //调用service层查询要修改的字典类型
        DictType dictType = dictTypeService.queryDictTypeByCode(code);
        //存到request作用域
        request.setAttribute("dictType", dictType);
        //跳转页面
        return "settings/dictionary/type/edit";
    }

    /**
     * @param dictType:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存修改后的字典类型信息
     * @date 2022/6/20 11:18
     */
    @RequestMapping("/settings/dictionary/type/saveEditDictType.do")
    @ResponseBody
    public Object saveEditDictType(DictType dictType) {
        ObjectForReturn objectForReturn = null;
        try {
            //调用service层完成编辑
            int affectedRows = dictTypeService.saveEditDictType(dictType);
            //根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param code:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 根据前台输入的code，查询字典类型的编码值是否已经存在
     * @date 2022/6/27 10:00
     */
    @RequestMapping("/settings/dictionary/type/isExistDictType.do")
    @ResponseBody
    public Object isExistDictType(String code) {
        //调用service层查询数据
        DictType dictType = dictTypeService.queryDictTypeByCode(code);

        //根据查询结果生成响应信息
        ObjectForReturn objectForReturn = new ObjectForReturn();
        if (dictType != null) {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
        } else {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
        }

        return objectForReturn;
    }

    //*************************************DictValue相关********************************************

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * 显示字典值主页
     * @date 2022/6/18 21:25
     */
    @RequestMapping("/settings/dictionary/value/valueIndex.do")
    public String valueIndex(HttpServletRequest request) {
        //调用DictValueService查询所有数据
        List<DictValue> dictValueList = dictValueService.queryAllDictValue();
        //放入request作用域
        request.setAttribute("dictValueList", dictValueList);
        //服务器内部转发跳转页面
        return "settings/dictionary/value/index";
    }

    /**
     * @param request:
     * @return String
     * @author xulingrui
     * @description TODO
     * 控制跳转到创建字典值页面
     * 并且把该页面所需的字典类型信息放入请求作用域
     * @date 2022/6/21 12:05
     */
    @RequestMapping("/settings/dictionary/value/toCreateDictValue.do")
    public String toCreateDictValue(HttpServletRequest request) {
        //调用service层，查询所有字典类型
        List<DictType> dictTypeList = dictTypeService.queryAllDictType();
        //放进requestScope
        request.setAttribute("dictTypeList", dictTypeList);
        //页面跳转
        return "settings/dictionary/value/save";
    }

    /**
     * @param dictValue:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存创建的字典值
     * @date 2022/6/21 12:05
     */
    @RequestMapping("/settings/dictionary/value/saveCreateDictValue.do")
    @ResponseBody
    public Object saveCreateDictValue(DictValue dictValue) {
        //封装参数
        dictValue.setId(UUIDUtils.generateUUID());
        System.out.println(dictValue);

        ObjectForReturn objectForReturn = null;
        try {
            //调用service层，保存新创建的DictValue
            int affectedRows = dictValueService.saveCreateDictValue(dictValue);

            //根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param id:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 前端发送ajax请求的参数形式为id=xXX&id=yYY&...id=zZZ
     * 会被SpringMVC自动解析成变量名为id的字符串数组
     * 删除字符串数组中id的字典值
     * @date 2022/6/21 10:07
     */
    @RequestMapping("/settings/dictionary/value/deleteDictValue.do")
    @ResponseBody
    public Object deleteDictValue(String[] id) {
        ObjectForReturn objectForReturn = null;
        try {
            //调用service层删除指定id的字典值
            int affectedRows = dictValueService.deleteDictValueByIds(id);

            //根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == id.length) {
                //数据库中删掉的记录数与前台选中的记录条数一致，删除成功
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    /**
     * @param id:
     * @param request:
     * @return String
     * @author xulingrui
     * @description TODO
     * 负责跳转至编辑字典值页面
     * 根据前端选择的字典值的id，查询出该字典值信息；以及所有字典类型信息
     * 放入request作用域，供前端解析渲染
     * @date 2022/6/21 12:06
     */
    @RequestMapping("/settings/dictionary/value/toEditDictValue.do")
    public String toEditDictValue(String id, HttpServletRequest request) {
        //调用service层，查询所有字典类型编码
        List<DictType> dictTypeList = dictTypeService.queryAllDictType();
        //根据id查询要编辑的字典值
        DictValue dictValue = dictValueService.queryDictValueById(id);

        //放进requestScope
        request.setAttribute("dictTypeList", dictTypeList);
        request.setAttribute("dictValue", dictValue);
        return "settings/dictionary/value/edit";
    }

    /**
     * @param dictValue:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存编辑后的字典值
     * @date 2022/6/21 12:08
     */
    @RequestMapping("/settings/dictionary/value/saveEditDictValue.do")
    @ResponseBody
    public Object saveEditDictValue(DictValue dictValue) {
        ObjectForReturn objectForReturn = null;
        try {
            //调用service层，保存更改后的字典值
            int affectedRows = dictValueService.saveEditDictValue(dictValue);

            //根据结果生成响应信息
            objectForReturn = new ObjectForReturn();
            if (affectedRows == 1) {
                //更新成功
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                //更新失败
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

}
