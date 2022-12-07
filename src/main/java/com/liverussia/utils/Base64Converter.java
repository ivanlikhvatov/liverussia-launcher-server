package com.liverussia.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Converter {
//    public static String encodeFileToBase64(File file) {
//
//        byte[] encoded;
//
//        try{
//            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return StringUtils.EMPTY;
//        }
//
//        return new String(encoded, StandardCharsets.US_ASCII);
//    }

    public static String encodeFileToBase64(byte[] file) {
        return Base64.getEncoder().encodeToString(file);
    }
}
