package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportApiController {
    @Autowired
    private ReportDto dto;
    @ApiOperation(value = "Gets sales report")
    @RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
    public List<SalesReportData> get(@RequestBody SalesReportForm form) throws ApiException {
        return dto.getSalesReport(form);
    }

    @ApiOperation(value = "Get inventory Report")
    @RequestMapping(path = "/api/reports/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> get() throws ApiException{
        return dto.getInventoryReport();
    }

    @ApiOperation(value = "Get daily sales Report")
    @RequestMapping(path = "/api/reports/day-sales", method = RequestMethod.GET)
    public List<DaySalesData> getDaySales() throws ApiException{
        return dto.getDaySalesReport();
    }
}
