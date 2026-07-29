package Io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用 BufferedInputStream 和 BufferedOutputStream 复制图片
 */
public class ImageCopy {
    public static void main(String[] args) {
        String srcPath = "src/Io/pic.png";
        String destPath = "src/Io/pic_copy.png";

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcPath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destPath))) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            System.out.println("图片复制成功：" + destPath);

        } catch (IOException e) {
            System.err.println("图片复制失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
