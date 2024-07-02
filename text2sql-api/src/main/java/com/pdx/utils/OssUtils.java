package com.pdx.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/*
 * @Author 派同学
 * @Description OSS文件存储工具类
 * @Date 2023/8/5
 **/
@Component
public class OssUtils {

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyid;

    @Value("${aliyun.oss.file.keysecret}")
    private String keysecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketname;


    public String uploadFileAvatar(MultipartFile file) {
        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String filename = file.getOriginalFilename();

            //1、在文件名称里添加一个随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid+filename;

            //2、把文件按照日期进行分类
            // 2021/7/17/xx.jpg
            //获取当前的日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath+"/"+filename;

            //调用OSS方法实现上传
            //第一个参数 Bucket名称
            //第二个参数  上传到OSS文件路径和文件名称
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketname, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://" + bucketname + "." + endpoint + "/"+filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
