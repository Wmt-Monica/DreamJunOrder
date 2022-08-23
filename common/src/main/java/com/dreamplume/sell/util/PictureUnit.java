package com.dreamplume.sell.util;

import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @Classname PageUnit
 * @Description TODO
 * @Date 2022/4/23 12:31
 * @Created by 翊
 */

@Slf4j
public class  PictureUnit {

    // 图片的存储
    public static Map<String, String> storage(MultipartFile file) {
        try {
            String pictureIcon = IDGenerator.getInstance().getId();
            return ImgtuUtil.upload(file.getBytes(), pictureIcon, ContentType.IMAGE_PNG);
        } catch (IOException e) {
            log.info("【更新用户头像】图片解析失败");
            throw new SellException(SellErrorCode.PICTURE_ANALYSIS_FIAL);
        }
    }

    // 图片文件删除
    public static void deleteImage(String pictureFileName) {
        ImgtuUtil.delete(pictureFileName);
    }

}
