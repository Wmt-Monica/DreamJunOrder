package com.dreamplume.sell.controller;

import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @Classname ClientAPKDownController
 * @Description TODO
 * @Date 2022/5/21 12:11
 * @Created by 翊
 */

@RestController
@RequestMapping("/app")
@Slf4j
public class ClientAPKDownController {

    // 二维码下载软件地址
    @RequestMapping("/down")
    public ResultVO<Object> downLoadApp(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //apk文件的路径
        String apkPath = "/project/apk";
        String apkName = "DreamJunOrder.apk";
        String fileName = "DreamJunOrder.apk";
        // 获得请求头中的User-Agent
        String agent = request.getHeader("User-Agent");
        // 根据不同的客户端进行不同的编码
        String filenameEncoder = "";
        if (agent.contains("MSIE")) {
            //为了单独解决火狐浏览器下载不显示中文或者乱码
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器 编码
            Base64.Encoder encoder = Base64.getEncoder();
            //BASE64 解码
            filenameEncoder = "=?utf-8?B?" + Arrays.toString(encoder.encode(apkName.getBytes(StandardCharsets.UTF_8))) + "?=";
            //为了单独安卓手机解决火狐浏览器下载不显示中文或者乱码========无语！！！！！！！
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "iso8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".apk");
        } else {
            // 其它浏览器
            filenameEncoder = URLEncoder.encode(apkName, "utf-8");
            //为了单独解决火狐浏览器下载不显示中文或者乱码
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
        }
        //apk文件的绝对路径
        String apkPathfile =apkPath + "/" + apkName;
        //new 一个apk的文件对象
        File file = new File(apkPathfile);
        try {
            if(file.exists()){
                downloadFile(file,response);
            }
        }catch(Exception e) {
            log.error("客户端下载失败");
            return ResultVOUtil.error(SellErrorCode.APP_DOWN_FAIL);
        }
        return ResultVOUtil.success();
    }

    private static void downloadFile(File file, HttpServletResponse response ){
        OutputStream os = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            //当前只下载apk格式文件 contentType
            response.setHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Length", "" + file.length());
            FileInputStream fileInputStream = new FileInputStream(file);
            WritableByteChannel writableByteChannel = Channels.newChannel(os);
            FileChannel fileChannel = fileInputStream.getChannel();
            fileChannel.transferTo(0,fileChannel.size(),writableByteChannel);
            fileChannel.close();
            os.flush();
            writableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
