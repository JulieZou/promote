package com.ibicd.promote.file;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 文件流和Base64 互转
 */
public class FileStreamBase64Interchange {

    public static void main(String[] args) {
        String imgFile = "D:\\testpic\\timg.png";
        String imageStr = getBase64Str(imgFile);
        String subString = "";
        if (imageStr.substring(0, 23).indexOf("base64") != -1) {
            subString = imageStr.substring(22);
        } else {
            subString = imageStr;
        }
        System.out.println("获取图片的base64位字符串： " + subString);

        getImageFile(subString);
    }


    /**
     * 获取图片的Base64编码
     *
     * @param base64Str
     */
    public static void getImageFile(String base64Str) {
        OutputStream out = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(base64Str);

            for (int i = 0; i < bytes.length; i++) {
                //调整异常数据
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            //生成新图片
            String imgFilePath = "D:\\testpic\\new.png";
            out = new FileOutputStream(imgFilePath);
            out.write(bytes);
        } catch (Exception e) {
            throw new RuntimeException("转换异常！");
        } finally {
            try {
                out.close();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getBase64Str(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BASE64Encoder().encode(data);
    }


}
