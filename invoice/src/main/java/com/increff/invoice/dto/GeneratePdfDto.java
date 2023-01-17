package com.increff.invoice.dto;

import com.increff.invoice.model.OrderDetailsData;
import com.increff.invoice.util.GeneratePDF;
import com.increff.invoice.util.GenerateXML;
import org.apache.fop.apps.FOPException;
import org.springframework.stereotype.Component;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Component
public class GeneratePdfDto {
    public String getEncodedPdf(OrderDetailsData orderDetailsData) throws IOException, FOPException, TransformerException, ParserConfigurationException {
        GenerateXML.createXml(orderDetailsData);
        GeneratePDF.createPDF();
        byte[] encodedBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File("bill.pdf"));
        String b64PDF = Base64.getEncoder().encodeToString(encodedBytes);
        return b64PDF;
    }
}
