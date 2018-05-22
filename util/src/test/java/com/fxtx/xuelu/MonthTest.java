package com.fxtx.xuelu;

import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: wugong.jie
 * \* Date: 2017/12/13 18:43
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class MonthTest {

    @Test
    public void monthTest(){
        MultipartFile multipartFile = new MultipartFile() {
            public String getName() {
                return null;
            }

            public String getOriginalFilename() {
                return null;
            }

            public String getContentType() {
                return null;
            }

            public boolean isEmpty() {
                return false;
            }

            public long getSize() {
                return 0;
            }

            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            public InputStream getInputStream() throws IOException {
                return null;
            }

            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }

}