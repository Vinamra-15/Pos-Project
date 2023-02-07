const dayDate= ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
function getDailySalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/day-sales";
}

function filterSalesReport() {
    var url = getDailySalesReportUrl();
    ajaxCall(url,"GET",{},(response)=>displaySalesReport(response))
}

function displaySalesReport(data) {
    $('#numberOfResults').append("Showing " + data.length + " results :")
    var $tbody = $('#daily-sales-table').find('tbody');
    $tbody.empty();
    for(var i in data){
        var item = data[i];
        var row = '<tr class="text-center">'
        + '<td>' + convertTimeStampToDateTime(item.date) + '</td>'
        + '<td>' + item.invoiced_orders_count + '</td>'
        + '<td>' + item.invoiced_items_count + '</td>'
        + '<td>' + numberWithCommas(item.total_revenue.toFixed(2)) + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
}

//INITIALIZATION CODE
function init(){
   $('#get-daily-sales-report').click(filterSalesReport);
   $('#reports-link').addClass('active').css("border-bottom","0.125rem solid black")
}
function convertTimeStampToDateTime(timestamp) {
    var date = new Date(timestamp);
    return (
      date.getDate() +
      "/" +
      (date.getMonth() + 1) +
      "/" +
      date.getFullYear() +
      " (" +
      dayDate[date.getDay()] + ")"
    );
  }

$(document).ready(init);
$(document).ready(filterSalesReport);