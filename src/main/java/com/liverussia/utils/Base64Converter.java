package com.liverussia.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Base64Converter {



//    public static String convertBase64ToPdf(ApproveDiseaseRequest request) throws IOException {
//
//        String dataDir = "/Users/ivanlikhvatov/Downloads/";
//
//        String base64 = request.getScannedCertificate();
//        String base64ImageString = base64.replace("data:application/pdf;base64,", "");
//        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageString);
//
//        String path = dataDir + "Base64_to_Pdf.pdf";
//
//        try (FileOutputStream fos = new FileOutputStream(path)) {
//            fos.write(imageBytes);
//        }
//
//        return null;
//
//
//    }

    public static String encodeFileToBase64(String path) {
        File file = new File(path);

        byte[] encoded;

        try{
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }

        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
