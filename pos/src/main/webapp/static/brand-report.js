function getbrandReportUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/brands";
}

function getbrandReport() {
  var url = getbrandReportUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displaybrandReportList(data);
    },
    error: handleAjaxError,
  });
}

function displaybrandReportList(data) {
  var $tbody = $("#brandCategory-table").find("tbody");
  $tbody.empty();
  for (var i in data) {
    var e = data[i];
    var row =
      "<tr>" +
      "<td class='text-center'>" +
      e.brand +
      "</td>" +
      "<td class='text-center'>" +
      e.category +
      "</td>" +
      "</tr>";
    $tbody.append(row);
  }
}

//INITIALIZATION CODE
function init() {
  $("#refresh-data").click(getbrandReport);
  $('#reports-link').addClass('active').css("border-bottom","2px solid black")
}

$(document).ready(init);
$(document).ready(getbrandReport);