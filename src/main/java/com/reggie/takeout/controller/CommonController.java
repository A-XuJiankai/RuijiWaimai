package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.utils.minio.MinioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author gett
 * @Date 2022/4/17  8:48
 * @Description 文件上传与下载
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Value("${reggie.path}")
    private String basePath;  //本地路徑

    @Autowired
    MinioUtil minioUtil;

    /**
     * @Description 文件上传--minio
     * @Author gett
     */
    @RequestMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {

        logger.info("文件上传入参{}", file.toString());

        try {

            JSONObject upload = minioUtil.upload(file);
            return R.success(upload.getString("objectName"));

        } catch (Exception e) {
            logger.info("文件上传异常{}", e);
            return R.error("文件上传异常");
        }

    }


    /**
     * @Description 文件下载--minio
     * @Author gett
     */
    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {

        logger.info("文件下载入参{}", name);

        try {

            minioUtil.download(response,name);

        } catch (Exception e) {
            logger.info("文件下载异常{}", e);
        }

    }


    /**
     * @Description 文件上传--本地
     * @Author gett
     */
    //@RequestMapping("/upload")
    //public R<String> upload(MultipartFile file) {
    //
    //    //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
    //    logger.info("文件上传入参{}", file.toString());
    //
    //    try {
    //
    //        //原始文件名
    //        String originalFilename = file.getOriginalFilename();
    //
    //        //后缀
    //        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
    //
    //        //使用uuid重新生成文件名，防止文件名称重复造成文件覆盖
    //        String fileName = IdUtil.getId()+substring;
    //
    //        File dir = new File(basePath);
    //        if (!dir.exists()){
    //            //目录不存在，创建目录
    //            dir.mkdir();
    //        }
    //
    //        //将临时文件转存到指定目录
    //        file.transferTo(new File(basePath + fileName));
    //
    //        return R.success(fileName);
    //
    //    } catch (Exception e) {
    //        logger.info("文件上传异常{}", e);
    //        return R.error("文件上传异常");
    //    }
    //}

    /**
     * @Description 文件下载--本地
     * @Author gett
     */
    //@RequestMapping("/download")
    //public void download(String name, HttpServletResponse response) throws IOException {
    //
    //    logger.info("文件下载入参{}",name);
    //
    //    ServletOutputStream outputStream=null;
    //    FileInputStream fileInputStream=null;
    //
    //    try {
    //        //输入流。通过输入流获取文件
    //        fileInputStream = new FileInputStream(new File(basePath + name));
    //
    //        //输出流。浏览器展示
    //         outputStream = response.getOutputStream();
    //
    //        int len=0;
    //        byte[] bytes = new byte[1024];
    //        while ((len= fileInputStream.read(bytes))!=-1){
    //            outputStream.write(bytes,0,len);
    //            outputStream.flush();
    //        }
    //
    //
    //
    //    }catch (Exception e) {
    //        logger.info("文件下载异常{}", e);
    //    }finally {
    //        outputStream.close();
    //        fileInputStream.close();
    //    }
    //
    //}


}
