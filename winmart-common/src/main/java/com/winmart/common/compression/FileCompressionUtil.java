package com.winmart.common.compression;

import com.winmart.common.file.CustomMultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCompressionUtil {

    public static MultipartFile compressFile(MultipartFile inputFile) throws IOException {
        String originalFileName = inputFile.getOriginalFilename();
        if (originalFileName == null) {
            return inputFile;
        }

        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i + 1).toLowerCase();
        }

        // Nếu không phải ảnh (jpg, jpeg, png) thì trả về file gốc
        if (!"jpg".equals(extension) && !"jpeg".equals(extension) && !"png".equals(extension)) {
            return inputFile;
        }

        // Tạo file tạm để xử lý
        File inputTempFile = File.createTempFile("input-", "." + extension);
        inputFile.transferTo(inputTempFile);

        File outputTempFile = File.createTempFile("output-", "." + extension);
        Thumbnails.of(inputTempFile)
                .size(800, 600)
                .outputQuality(0.8)
                .toFile(outputTempFile);

        InputStream is = new FileInputStream(outputTempFile);
        MultipartFile result = new CustomMultipartFile(
                originalFileName,
                originalFileName,
                inputFile.getContentType(),
                is
        );
        is.close();

        // Dọn dẹp file tạm
        inputTempFile.delete();
        outputTempFile.delete();

        return result;
    }
}
