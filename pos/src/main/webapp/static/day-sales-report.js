function getDailySalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/day-sales";
}

function filterSalesReport() {
    var url = getDailySalesReportUrl();
    console.log(url);
    $.ajax({
       url: url,
       type: 'GET',
       success: function(response) {
            console.log(response);
            displaySalesReport(response);
       },
       error: handleAjaxError
    });
}

function displaySalesReport(data) {
    var $tbody = $('#daily-sales-table').find('tbody');
    $tbody.empty();
    for(var i in data){
        var item = data[i];
        var row = '<tr class="text-center">'
        + '<td>' + convertTimeStampToDateTime(item.date) + '</td>'
        + '<td>' + item.invoiced_orders_count + '</td>'
        + '<td>' + item.invoiced_items_count + '</td>'
        + '<td>' + item.total_revenue + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
}

//INITIALIZATION CODE
function init(){
   $('#get-daily-sales-report').click(filterSalesReport);
}

function convertTimeStampToDateTime(timestamp) {
    var date = new Date(timestamp);
    return (
      date.getDate() +
      "/" +
      (date.getMonth() + 1) +
      "/" +
      date.getFullYear() +
      " " +
      date.getHours() +
      ":" +
      date.getMinutes() +
      ":" +
      date.getSeconds()
    );
  }

$(document).ready(init);
$(document).ready(filterSalesReport);