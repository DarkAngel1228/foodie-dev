package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "useId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 定义保存地址
        String fileSpace = fileUpload.getImageUserFaceLocation();

        // 在路径上尉每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                String filename = file.getOriginalFilename();

                System.out.println("第一处" + filename);
                if (StringUtils.isNotBlank(filename)) {
                    String[] fileNameArr = filename.split("\\.");
                    // 文件后缀
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (!suffix.equalsIgnoreCase("png") && !suffix.equalsIgnoreCase("jpg") && !suffix.equalsIgnoreCase(
                            "jpeg")) {
                        return IMOOCJSONResult.errorMsg("图片格式不正确");
                    }

                    String newFileName = "face-" + userId + "." + suffix;
                    System.out.println("第2处" + newFileName);
                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    System.out.println("第3处" + finalFacePath);
                    uploadPathPrefix += (File.separator + newFileName);
                    System.out.println("第4处" + uploadPathPrefix);
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        outFile.getParentFile().mkdirs();
                        System.out.println("第5处");
                    }

                    // 文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    int i = IOUtils.copy(inputStream, fileOutputStream);
                    System.out.println("第6");
                    System.out.println(i);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空!");
        }

        String imageServerUrl = fileUpload.getImageServerUrl();
        String finalUserFaceUrl =
                imageServerUrl + uploadPathPrefix + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        Users user = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        //user = setNullProperty(user);
        // 增加令牌token，会整合进redis，分布式会话
        UsersVO usersVO = conventUsersVO(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);


        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return

        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        // userResult = setNullProperty(userResult);
        // 增加令牌token，会整合进redis，分布式会话
        UsersVO usersVO = conventUsersVO(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }

    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();

        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            String errorField = error.getField();
            String errMsg = error.getDefaultMessage();
            map.put(errorField, errMsg);
        }

        return map;
    }


    private Users setNullProperty(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);

        return user;
    }







































}
