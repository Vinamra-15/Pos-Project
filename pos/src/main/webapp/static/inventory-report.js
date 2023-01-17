function getInventoryReportUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/reports/inventory";
}

function getInventoryReport() {
  var url = getInventoryReportUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayInventoryReportList(data);
    },
    error: handleAjaxError,
  });
}

//UI DISPLAY METHODS
function displayInventoryReportList(data) {
  var $tbody = $("#inventory-report-table").find("tbody");
  $tbody.empty();
  for (var i in data) {
    var e = data[i];
    var row =
      "<tr>" +
      "<td  class='text-center'>" +
      e.brand +
      "</td>" +
      "<td  class='text-center'>" +
      e.category +
      "</td>" +
      "<td  class='text-center'>" +
      e.quantity +
      "</td>" +
      "</tr>";
    $tbody.append(row);
  }
}

//INITIALIZATION CODE
function init() {
  $("#refresh-data").click(getInventoryReport);
}

$(document).ready(init);
$(document).ready(getInventoryReport);