package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath+"/"+savedFileName;
        FileOutputStream fos  =new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName; //업로드된 파일의 이름을 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); // 파일이 저장된경로를 이용하여 파일객체를 생성

        if(deleteFile.exists()){ //해당파일이 존재하면 삭제
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
