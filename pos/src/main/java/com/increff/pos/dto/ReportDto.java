package com.increff.pos.dto;

import com.increff.pos.model.Data.DaySalesData;
import com.increff.pos.model.Data.InventoryReportData;
import com.increff.pos.model.Data.SalesReportData;
import com.increff.pos.model.Form.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.increff.pos.util.ConvertUtil.*;

@Component
public class ReportDto {
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ReportService reportService;

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        Date startDate = salesReportForm.getStartDate();
        Date endDate = salesReportForm.getEndDate();

        List<OrderPojo> orderPojos = orderService.getOrderByStartDateEndDate(startDate,endDate);
        List<OrderItemPojo> orderItemPojos = new ArrayList<OrderItemPojo>();
        List<BrandCategoryPojo> brandCategoryPojos = brandCategoryService.getAlikeBrandCategory(salesReportForm.getBrand(),salesReportForm.getCategory());
        for(OrderPojo orderPojo:orderPojos){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsByOrderId(orderPojo.getId());
            orderItemPojos.addAll(orderItemPojoList);
        }
        return getSalesReportData(brandCategoryPojos,orderItemPojos);
    }

    public List<InventoryReportData> getInventoryReport() throws ApiException {
        Map<Integer,InventoryReportData> brandIdToInventoryReportDataMap = new HashMap<Integer,InventoryReportData>();
        List<ProductPojo> productPojos = productService.getAllProducts();
        for(ProductPojo productPojo:productPojos){
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(productPojo.getBrandId());
            if(!brandIdToInventoryReportDataMap.containsKey(productPojo.getBrandId())) {
                InventoryReportData inventoryReportData = new InventoryReportData(brandCategoryPojo.getBrand(),brandCategoryPojo.getCategory(),(Integer) 0);
                brandIdToInventoryReportDataMap.put(productPojo.getBrandId(), inventoryReportData);
            }
            InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
            InventoryReportData inventoryReportData = brandIdToInventoryReportDataMap.get(productPojo.getBrandId());
            inventoryReportData.setQuantity(inventoryReportData.getQuantity()+inventoryPojo.getQuantity());
            brandIdToInventoryReportDataMap.put(productPojo.getBrandId(),inventoryReportData);
        }
        return convertMaptoInventoryReportDataList(brandIdToInventoryReportDataMap);
    }

    @Scheduled(cron = "0 49 15 * * ?")
    public DaySalesPojo generateDailySalesReport() throws ApiException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date yesterday = TimeUtil.getStartOfDay(calendar.getTime());

        List<OrderPojo> orderPojoList = orderService.getOrderByStartDateEndDate(yesterday, new Date());
        List<OrderItemPojo> orderItemPojoList = getOrderItemPojosFromorderPojos(orderPojoList);

        DaySalesPojo daySalesPojo = new DaySalesPojo(new Date(),orderPojoList.size(),orderItemPojoList.size(),calculateRevenue(orderItemPojoList));
        reportService.addDaySales(daySalesPojo);
        return daySalesPojo;
    }
    public List<DaySalesData> getDaySalesReport(){
        return convert(reportService.getDaySales());
    }
    private Double calculateRevenue(List<OrderItemPojo> orderItemPojoList){
        Double totalRevenue = 0d;
        for (OrderItemPojo orderItem : orderItemPojoList) {
            totalRevenue += orderItem.getSellingPrice() * orderItem.getQuantity();
        }
        return totalRevenue;
    }

    private List<OrderItemPojo> getOrderItemPojosFromorderPojos(List<OrderPojo> orderPojoList) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        for(OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojo = orderItemService.getOrderItemsByOrderId(orderPojo.getId());
            orderItemPojoList.addAll(orderItemPojo);
        }
        return orderItemPojoList;
    }
    private List<SalesReportData> getSalesReportData(List<BrandCategoryPojo> brandCategoryPojos,
                                                     List<OrderItemPojo> orderItemPojos) throws ApiException {
        Map<Integer,SalesReportData> brandSalesMapping= initializeBrandSalesMap(brandCategoryPojos);
        for(OrderItemPojo orderItemPojo:orderItemPojos){
            ProductPojo productPojo = productService.getProduct(orderItemPojo.getProductId());
            if(brandSalesMapping.containsKey(productPojo.getBrandId())) {
                SalesReportData salesReportData = brandSalesMapping.get(productPojo.getBrandId());
                salesReportData.setQuantity(salesReportData.getQuantity() + orderItemPojo.getQuantity());
                salesReportData.setRevenue(salesReportData.getRevenue() + orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                brandSalesMapping.put(productPojo.getBrandId(), salesReportData);
            }
        }
        return convertMapToSalesReportDataList(brandSalesMapping);
    }
    private Map<Integer,SalesReportData> initializeBrandSalesMap(List<BrandCategoryPojo> brandCategoryPojos){
        Map<Integer,SalesReportData> brandSalesMapping = new HashMap<>();
        for(BrandCategoryPojo brandCategoryPojo:brandCategoryPojos){
            SalesReportData salesReportData = new SalesReportData(brandCategoryPojo.getBrand(),brandCategoryPojo.getCategory(),(Integer) 0,(Double) 0.0);
            brandSalesMapping.put(brandCategoryPojo.getId(),salesReportData);
        }
        return brandSalesMapping;
    }
}
