function getSalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/sales";
}

function filterSalesReport() {
    var $form = $("#sales-form");
    var json = toJson($form);
    var url = getSalesReportUrl();
//    console.log(url);

    $.ajax({
       url: url,
       type: 'POST',
       data: json,
       headers: {
        'Content-Type': 'application/json'
       },
       success: function(response) {
//            console.log(response);
            displaySalesReport(response);
       },
       error: handleAjaxError
    });
}

function displaySalesReport(data) {
    var $tbody = $('#sales-table').find('tbody');
    $tbody.empty();

    if(data.length===0)
    {
        $('#sales-table').hide()
    }
    else
    {
        $('#sales-table').show()
    }
    for(var i in data){
        let srNo = Number.parseInt(i) + 1
        var b = data[i];
        var row = '<tr>'
        + '<td>'+srNo+'</td>'
        + '<td>' + b.brand + '</td>'
        + '<td>' + b.category + '</td>'
        + '<td>' + b.quantity + '</td>'
        + '<td>' + numberWithCommas(b.revenue.toFixed(2)) + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
}

//INITIALIZATION CODE
function init(){
   $('#filter-sales-report').click(filterSalesReport);
   $('#reports-link').addClass('active').css("border-bottom","2px solid black")
   displaySalesReport([])
}

$(document).ready(init);