package com.springboot.zhiyi.controller;

import cn.hutool.core.io.FileUtil;
import com.springboot.zhiyi.common.BaseResponse;
import com.springboot.zhiyi.common.ErrorCode;
import com.springboot.zhiyi.common.ResultUtils;
import com.springboot.zhiyi.config.MinioConfig;
import com.springboot.zhiyi.constant.FileConstant;
import com.springboot.zhiyi.exception.BusinessException;
import com.springboot.zhiyi.manager.CosManager;
import com.springboot.zhiyi.model.dto.file.UploadFileRequest;
import com.springboot.zhiyi.model.entity.User;
import com.springboot.zhiyi.model.enums.FileUploadBizEnum;
import com.springboot.zhiyi.service.UserService;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.springboot.zhiyi.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
   
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private MinioUtils minioUtils;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 文件上传
     *
     * @param multipartFile 上传的文件
     * @param uploadFileRequest 上传请求参数
     * @param request HTTP请求
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 验证业务类型
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 验证文件
        validFile(multipartFile, fileUploadBizEnum);

        // 获取登录用户
        User loginUser = userService.getLoginUser(request);

        try {
            // 生成文件名: 根据业务、用户来划分 + 随机字符串 + 原始文件名
            String uuid = RandomStringUtils.randomAlphanumeric(8);
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = StringUtils.substringAfterLast(originalFilename, ".");
            String newFileName = String.format("%s/%s/%s-%s.%s",
                    fileUploadBizEnum.getValue(),
                    loginUser.getId(),
                    uuid,
                    System.currentTimeMillis(),
                    fileExtension);

            // 上传文件到MinIO
            minioUtils.uploadFile(
                    minioConfig.getBucketName(),
                    multipartFile,
                    newFileName,
                    multipartFile.getContentType());

            // 获取预签名访问URL
            String fileUrl = minioUtils.getPresignedObjectUrl(
                    minioConfig.getBucketName(),
                    newFileName);

            return ResultUtils.success(fileUrl);
        } catch (Exception e) {
            log.error("file upload error, filepath", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
    }


    /**
     * 删除
     *
     * @param fileName
     */
    @DeleteMapping("/")
    public void delete(@RequestParam("fileName") String fileName) {
        minioUtils.removeFile(minioConfig.getBucketName(), fileName);
    }

    /**
     * 获取文件信息
     *
     * @param fileName
     * @return
     */
    @GetMapping("/info")
    public String getFileStatusInfo(@RequestParam("fileName") String fileName) {
        return minioUtils.getFileStatusInfo(minioConfig.getBucketName(), fileName);
    }

    /**
     * 获取文件外链
     *
     * @param fileName
     * @return
     */
    @GetMapping("/url")
    public String getPresignedObjectUrl(@RequestParam("fileName") String fileName) {
        return minioUtils.getPresignedObjectUrl(minioConfig.getBucketName(), fileName);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            InputStream fileInputStream = minioUtils.getObject(minioConfig.getBucketName(), fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载失败");
        }
    }



    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
