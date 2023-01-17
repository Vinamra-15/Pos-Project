package com.increff.pos.util;

import com.increff.pos.model.OrderDetailsData;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtil {
    public static String generatePDF(OrderDetailsData orderDetailsData) throws IOException {
        String encodedPdf = getEncodedPdf(orderDetailsData);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(encodedPdf);
        String filePath = "D:\\IncreffProjectPOS BILLS\\bills\\"  + orderDetailsData.getId().toString() + ".pdf";
        File file = new File(filePath);
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(decodedBytes);
        fop.flush();
        fop.close();
        return filePath;
    }

    private static String getEncodedPdf(OrderDetailsData orderDetailsData){
        RestTemplate restTemplate = new RestTemplate();
        String invoiceAppUrl = "http://localhost:8000/invoice";
        String response = restTemplate.postForObject(invoiceAppUrl + "/api/generate",orderDetailsData, String.class);
        return response;
    }
}
