package com.increff.pos.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderDetailsData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

@Api
@RestController
public class FileApiController {
    @Autowired
    private OrderDto orderDto;
    @RequestMapping(path = "/download/invoice/{orderId}", method = RequestMethod.GET)
    public Resource download(@PathVariable Integer orderId) throws ApiException {
        try{
              return orderDto.getFileResource(orderId);
        }
        catch (IOException exception){
            throw new ApiException("Pdf being generated!");
        }
    }
}
