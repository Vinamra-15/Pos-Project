package com.increff.invoice.controller;

import com.increff.invoice.dto.GeneratePdfDto;
import com.increff.invoice.model.OrderDetailsData;
import com.increff.invoice.util.GeneratePDF;
import com.increff.invoice.util.GenerateXML;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Api
@RestController
public class GeneratePdfController {
    @Autowired
    private GeneratePdfDto generatePdfDto;
    @ApiOperation(value = "Returns base64Encoded string")
    @RequestMapping(path = "/api/generate", method = RequestMethod.POST)
    public String getEncodedPdf(@RequestBody OrderDetailsData orderDetailsData) throws IOException, FOPException, TransformerException, ParserConfigurationException {
        return generatePdfDto.getEncodedPdf(orderDetailsData);
    }



}
