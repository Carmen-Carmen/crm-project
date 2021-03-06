package com.lingrui.crm.workbench.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.common.utils.POIUtils;
import com.lingrui.crm.common.utils.StringUtils;
import com.lingrui.crm.common.utils.UUIDUtils;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.UserService;
import com.lingrui.crm.workbench.domain.Activity;
import com.lingrui.crm.workbench.domain.ActivityRemark;
import com.lingrui.crm.workbench.service.ActivityRemarkService;
import com.lingrui.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 15:27
 * @ Version 1.0
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     *      查出市场活动主页面所需动态数据：所有用户信息
     *      并放到作用域里，保存到request作用域即可
     *      跳转到市场活动的主页面的index.jsp
     * @date 2022/5/23 15:28
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //1、查询所有用户信息并存到request作用域
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);

        //2、请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    /**
     * @param activity: 直接用实体类对象接收参数
     * @param session:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 保存创建市场活动
     * @date 2022/5/28 18:16
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody//返回json字符串
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        //使用实体类对象作为参数，直接封装表单提交的字段成一个对象
        //1、还要封装其他参数
        activity.setId(UUIDUtils.generateUUID());//生成一个32位uuid作为id
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));//以当前时间转化为字符串作为创建时间，注意需要的是日期+时间的datetime
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);//从session中取出当前用户
        activity.setCreateBy(sessionUser.getId());//数据库中市场活动的create_by字段理论上引用的也是用户id

        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
        //2、调用activityService，保存创建的市场活动
            int influencedRows = activityService.saveCreateActivity(activity);

        //3、根据处理结果，生成响应
            if (influencedRows > 0) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);//不要把后台报错的理由写的太清楚
            }
        } catch (Exception e) {
            e.printStackTrace();

            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }


        return objectForReturn;
    }

    /**
     * @param name:
     * @param owner:
     * @param startDate:
     * @param endDate:
     * @param pageNo:要查询的页码
     * @param pageSize:每页显示条数
     * @return Object
     * @author xulingrui
     * @description TODO
     * 分页，按条件查询出市场活动列表
     * 2022/6/1，改为使用PageHelper控制分页查询
     * @date 2022/5/28 18:17
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(
            //查询条件
            String name,
            String owner,
            String startDate,
            String endDate,
            //分页信息
            int pageNo,
            int pageSize
    ) {
        //封装参数，key要和sql语句中占位符的一致
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        //使用PageHelper后就不需要手动limit分页了
//        map.put("pageNo", pageNo);
//        map.put("pageSize", pageSize);
//        map.put("beginNo", (pageNo - 1) * pageSize);//计算出开始显示的条数，作为sql语句中limit的第一个参数

        //调用service，获取数据
//        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        PageHelper.startPage(pageNo, pageSize);//使用PageHelper控制分页查询了
        List<Activity> activityList = activityService.queryActivityByCondition(map);
//        int totalRows = activityService.queryCountOfActivityByCondition(map);
//        int totalPages;//计算总页数，扔给前端
//        if (totalRows % pageSize == 0) {
//            totalPages = totalRows / pageSize;
//        } else {
//            totalPages = totalRows / pageSize + 1;
//        }
        PageInfo<Activity> pageInfo = new PageInfo<>(activityList, 5);//前端的pagination中设置了连续显示卡片数为5

        //根据查询结果，生成响应信息
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("activityList", activityList);
        resultMap.put("totalRows", pageInfo.getTotal());
        resultMap.put("totalPages", pageInfo.getPages());

        return resultMap;
    }

    /**
     * @param id:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 前端发出请求中"id=xxx&...&id=xxx"会被dispatcherServlet转化为变量名为id的String数组
     * @date 2022/5/28 18:18
     */
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
            //调用service层方法，删除市场活动
            int affectedRows = activityService.deleteActivityByIds(id);
            //把判断删除条数是否与传入数组长度一致的判断放到service层，一旦出错就会被这里catch到
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
        } catch (Exception e) {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            e.printStackTrace();
        }

        return objectForReturn;
    }

    /**
     * @param id:
     * @return Object
     * @author xulingrui
     * @description TODO
     * 根据id查询市场活动，返回给前端页面用于修改modal窗口的数据回显
     * @date 2022/5/28 18:19
     */
    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        //调用Service方法，传入接收到的参数，获取市场活动实体类对象
        Activity activity = activityService.queryActivityById(id);
        //根据查询结果，返回响应值
        return activity;
    }


    /**
     * @param activity: 直接用实体类对象接收参数
     * @return Object
     * @author xulingrui
     * @description TODO
     * @date 2022/5/28 18:21
     */
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        //1、补全activity中缺少的数据
        //获取当前用户，将其id设为市场活动的editBy属性
        User user = (User) session.getAttribute(Constants.SESSION_USER);//一定能获取到的，因为做过登录验证了
        activity.setEditBy(user.getId());
        //将当前时间转化为字符串，设为editTime属性
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        int affectedRows = 0;

        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
        //2、调用service方法，保存修改后的市场活动
            affectedRows = activityService.saveEditActivity(activity);

        //3、根据处理结果生成响应信息
            if (affectedRows == 1) {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            } else {
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            }
        } catch (Exception e) {
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
            e.printStackTrace();
        }

        return objectForReturn;
    }

    /**
     * @param response:
     * @return void
     * @author xulingrui
     * @description TODO
     * 处理前端的测试页面filedownloadtest.jsp发来文件下载请求的控制器方法
     * @date 2022/5/30 15:00
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //1、设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //application/octet-stream是应用程序文件的默认值。意思是未知的应用程序文件 ，浏览器一般不会自动执行或询问执行。
        // 浏览器会像对待，设置了HTTP头Content-Disposition 值为 attachment 的文件一样来对待这类文件，即浏览器会触发下载行为。

        //不设置这个响应头信息的话，浏览器会自动调用能开启这类文件的软件，实在找不到才会以附件形式下载
        response.addHeader("Content-Disposition", "attachment;filename=" + "download.xls");//告知浏览器以附件形式打开，即下载！

        //2、获取输出流，相当于在后端和前端建立了一条管道
//        PrintWriter writer = response.getWriter();//这个输出流是字符流，只能写字符！
        ServletOutputStream sos = response.getOutputStream();

        //3、从服务器本地读取要发给前端的文件
        File file = new File("/Users/xulingrui/Downloads/output.xls");
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[256];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
        //4、写出
            sos.write(buffer);
        }

        //5、关闭资源
        //规则：谁new的谁关
        fis.close();//FileInputStream是我自己开启的，我自己关
        sos.flush();//ServletOutputStream是tomcat开启的，tomcat自己会关的；flush一下，把缓存区的内容输出
    }

    /**
     * @param response: 利用response的outputStream传输文件
     * @return void     因此返回值为void
     * @author xulingrui
     * @description TODO
     * 处理"批量导出市场活动"的控制器方法
     * 异常直接都抛出就行了
     * @date 2022/5/30 15:02
     */
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws Exception{
        //1、调用service层方法，查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivity();

        //2、创建excel文件
        HSSFWorkbook workbook = null;
        workbook = POIUtils.generateWorkbookByList(activityList, "市场活动列表", Constants.EXPORT_ACTIVITY_FIELD_NAME_LIST);
        if (workbook == null) return;//没有查出任何数据时

//        //3、将excel文件生成在服务器端
//        String fileName = UUIDUtils.generateUUID() + ".xls";
//        FileOutputStream fos = new FileOutputStream(Constants.SERVER_FILE_PATH + fileName);
//        workbook.write(fos);
//        //关闭资源
//        fos.close();
//        workbook.close();//workbook也是一个流资源！
//
//        //4、把生成的excel文件下载到客户端
//        response.setContentType("application/octet-stream;charset=UTF-8");//设置响应类型为应用程序文件
//        response.addHeader("Content-Disposition", "attachment;filename=export.xls");//设置客户端拿到文件之后的处置方式为：以附件形式下载
//        ServletOutputStream out = response.getOutputStream();
//        //从服务器中读取刚生成的excel文件，并通过ServletOutputStream写出
//        FileInputStream fis = new FileInputStream(Constants.SERVER_FILE_PATH + fileName);
//        byte[] buffer = new byte[256];
//        int len = 0;
//        while ((len = fis.read(buffer)) != -1) {
//        //写出
//            out.write(buffer);
//        }
//        //关闭资源
//        fis.close();
//        //刷新ServletOutputStream，冲出缓存区中数据
//        out.flush();

        //其实根本没有必要把workbook写到服务器磁盘上
        //3、将内存中的workbook直接写出到ServletOutputStream；内存 --> 内存，效率显著提高
        response.setContentType("application/octet-stream;charset=UTF-8");//设置响应类型为应用程序文件
        response.addHeader("Content-Disposition", "attachment;filename=export.xls");//设置客户端拿到文件之后的处置方式为：以附件形式下载
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        //资源的后续处置
        out.flush();
        workbook.close();
    }

    /**
     * @param id:
     * @param response:
     * @return void
     * @author xulingrui
     * @description TODO
     * 根据id字符串数组，查出所有对应id的市场活动，生成一个workbook
     * 利用workbook.write()方法，将response的outputStream作为参数传入，直接输出到前端页面去
     * @date 2022/6/1 17:29
     */
    @RequestMapping("/workbench/activity/exportSelectedActivity.do")
    public void exportSelectedActivity(String[] id, HttpServletResponse response) throws Exception {
        //1、获取市场活动数据
        List<Activity> activityList = activityService.queryActivityByIds(id);

        //2、生成workbook
        HSSFWorkbook workbook = POIUtils.generateWorkbookByList(activityList, "市场活动列表", Constants.EXPORT_ACTIVITY_FIELD_NAME_LIST);

        //3、写出到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");//设置响应类型为应用程序文件
        response.addHeader("Content-Disposition", "attachment;filename=export.xls");//设置客户端拿到文件之后的处置方式为：以附件形式下载
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        workbook.close();
    }

    /**
     * @param userName:
     * @param myFile: MultipartFile必须处于springMVC容器里，即在springMVC配置文件中配置文件上传解析器
     * @return Object
     * @author xulingrui
     * @description TODO
     * @date 2022/6/1 17:40
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(String userName, MultipartFile myFile) throws IOException {
        //把文本数据打印到控制台
        System.out.println("userName=" + userName);

        //把文件在服务器指定目录中生成一个同样的文件
        String fileName = myFile.getOriginalFilename();
        File file = new File("/Users/xulingrui/Desktop/java_learning/CRM_project/serverDir/" + fileName);
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(myFile.getBytes());
//        fos.close();
        //以上操作全部可以通过transferTo(File file)
        myFile.transferTo(file);

        //返回响应信息
        ObjectForReturn objectForReturn = new ObjectForReturn();
        objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
        objectForReturn.setMessage("上传成功");

        return objectForReturn;
    }

    /**
     * @param activityFile:
     * @param session: 需要从session中获取当前用户
     * @return Object
     * @author xulingrui
     * @description TODO
     * 接收用户上传的Excel文件
     * 解析Excel文件中数据，生成一个List
     * 对List中实体类对象校验，并去除不合规定数据
     * 将List传给service，调用mapper向数据库中写数据
     * 向前端返回实际导入数据条数等
     * @date 2022/6/5 16:35
     */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Constants.SESSION_USER);

        ObjectForReturn objectForReturn = new ObjectForReturn();
        try {
//        //1、把传输过来的Excel文件写到磁盘目录
//            String fileName = UUIDUtils.generateUUID() + ".xls";//使用UUID给文件名重新命名，避免在服务器磁盘上文件名冲突
//            File file = new File(Constants.SERVER_FILE_PATH + fileName);
//            activityFile.transferTo(file);
//        //2、解析excel文件，获取文件中的数据，并且封装成List<Activity>
//            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

            //当然也可以把MultipartFile转化为InputStream，作为参数直接传给new HSSFWorkbook
            //规避了磁盘的读写，效率显著提高
            HSSFWorkbook workbook = new HSSFWorkbook(activityFile.getInputStream());

            //不需要用户输入的数据：id、owner、createTime、createBy、editTIme、editBy
            List<Activity> activityList = POIUtils.parseWorkbookToList(workbook, Activity.class, Constants.IMPORT_ACTIVITY_FIELD_NAME_LIST);
            int originRowNum = activityList.size();

            //判断是否为有效数据
            List<Activity> errorRowList = new ArrayList<>();
            for (Activity activity : activityList) {
                if (StringUtils.isNull(activity.getName()) || StringUtils.isNull(activity.getCost())) {//必须填写活动名称和成本
                    errorRowList.add(activity);
                    continue;
                }
                //如果开始日期和结束日期都填了，进行校验
                if (StringUtils.isNotNull(activity.getStartDate()) && StringUtils.isNotNull(activity.getEndDate())) {
                    if (activity.getEndDate().compareTo(activity.getStartDate()) < 0) {
                        errorRowList.add(activity);
                        continue;
                    }
                }

                //走到这，说明成本肯定填了，就看输入的成本是否合法
                try {
                    int cost = Integer.parseInt(activity.getCost());
                    if (cost < 0) {
                        //成本为非负整数
                        errorRowList.add(activity);
                    }
                } catch (NumberFormatException e) {
                    //parseInt出异常，说明成本没写对
                    errorRowList.add(activity);
                }
            }
            activityList.removeAll(errorRowList);//排除错误数据

            for (Activity activity : activityList) {
                //填入id、owner、createTime、createBy
                activity.setId(UUIDUtils.generateUUID());
                activity.setOwner(sessionUser.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(sessionUser.getId());
            }
            //调用
            int affectedRowNum = activityService.saveActivityByList(activityList);
            Map<String, Object> retData = new HashMap<>();
            retData.put("affectedRowNum", affectedRowNum);
            retData.put("originRowNum", originRowNum);
            //3、根据处理结果生成响应信息
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
            objectForReturn.setReturnData(retData);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage(Constants.COMMON_ERROR_MSG);
        }

        return objectForReturn;
    }

    @RequestMapping("/workbench/activity/downloadTemplate.do")
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        //设置响应信息
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + Constants.IMPORT_ACTIVITY_TEMPLATE_FILE_NAME);
        ServletOutputStream sos = response.getOutputStream();

        //从服务器路径中取出模板
        FileInputStream fis = new FileInputStream(Constants.SERVER_FILE_PATH + Constants.IMPORT_ACTIVITY_TEMPLATE_FILE_NAME);
        byte[] buffer = new byte[256];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
        //写出
            sos.write(buffer);
        }

        //资源处置
        fis.close();
        sos.flush();
    }

    @RequestMapping("/workbench/activity/activityDetail.do")
    public String activityDetail(String id, HttpServletRequest request) {
        //调用service层，查询数据
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);

        //将查询出的数据放进request作用域
        request.setAttribute("activity", activity);
        request.setAttribute("remarkList", activityRemarkList);

        //请求转发
        return "workbench/activity/detail";
    }

}
