package com.reggie.takeout.utils.minio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.utils.IdUtil;
import com.reggie.takeout.utils.R;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author gett
 * @Date 2022/4/17  10:27
 * @Description minio
 */
@Component
public class MinioUtil {

    private static Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketname}")
    private String bucketName;


    //文件上传
    public JSONObject upload(MultipartFile file) throws  Exception{

        JSONObject res = new JSONObject();

        InputStream inputStream = null;
        try {

            //判断文件是否为空
            if (file == null || 0 == file.getSize()) {
                res.put("code", 1);
                logger.error("上传文件不能为空");
                return res;
            }

            //判断桶是否存在
            createBucket();

            //新的文件名
            String fileName = IdUtil.getId() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            res.put("code", 0);
            res.put("bucketName", bucketName);
            res.put("objectName", fileName);
            return res;

        } catch (Exception e) {
            logger.info("文件上传失败{}"+e.getMessage());
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //文件下载
    public void download(HttpServletResponse response, String fileName) throws Exception {
        InputStream inputStream = null;
        try {

            //判断桶是否存在
            if (!bucketExists()){
                logger.error("桶不存在");
                throw new BusinessException("桶不存在");
            }

            //获取文件源信息
            StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());

            if (statObject==null){
                logger.error("获取文件源信息失败");
                throw new BusinessException("获取文件源信息失败");
            }

            //设置响应的内容类型 --浏览器对应不同类型做不同处理
            response.setContentType(statObject.contentType());
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(fileName, "UTF-8"));

            //文件下载
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());

            //利用apache的IOUtils
            IOUtils.copy(inputStream, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //文件删除
    public boolean delete(String fileName) {
        try {

            //判断桶是否存在
            if (bucketExists()){
                logger.error("桶不存在");
                throw new BusinessException("桶不存在");
            }

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                    .object(fileName).build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //桶是否存在
    public boolean bucketExists() {
        boolean b = false;
        try {
            b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (b) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //创建桶
    public void createBucket() throws Exception {
        try {
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!b) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            logger.info("上传创建桶失败{}"+e.getMessage());
            throw e;
        }
    }

    //获取桶列表
    public List getBucketList() throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        List list = new ArrayList();
        for (Bucket bucket : buckets) {
            String name = bucket.name();
            list.add(name);
        }
        return list;
    }

    //获取指定bucketName下所有文件
    public List<Object> getFolderList(String bucketName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = results.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','fileSize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse((String.format(format, item.objectName(),
                    formatFileSize(item.size())))));
        }
        return items;
    }

    /**
     * 将块文件合并到新桶  块文件必须满足 名字是 0 1 2 3 5(方便排序).. 合并文件前排列一下块文件的顺序(升序)  怎么切的顺序就怎么合的顺序
     *
     * @param bucketName  存块文件的桶
     * @param bucketName1 存新文件的桶
     * @param fileName1   存到新桶中的文件名称
     * @return
     */
    public boolean merge(String bucketName, String bucketName1, String fileName1) {
        try {
            List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
            List<Object> folderList = this.getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (folderList != null && !folderList.isEmpty()) {
                for (int i = 0; i < folderList.size(); i++) {
                    Map o = (Map) folderList.get(i);
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                Collections.sort(fileNames, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if (Integer.parseInt(o2) > Integer.parseInt(o1)) {
                            return -1;
                        }
                        return 1;
                    }
                });
                for (String name : fileNames) {
                    sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(name).build());
                }
            }
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName1)
                            .object(fileName1)
                            .sources(sourceObjectList)
                            .build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }
}
