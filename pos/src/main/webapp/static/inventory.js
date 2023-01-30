function getInventoryUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/inventory";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}


function updateInventory(event){
   var barcode = $("#inventory-edit-form input[name=barcode]").val();
   var url = getInventoryUrl() +"/" + barcode;
   var $form = $("#inventory-edit-form");
   var json = toJson($form);
   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getInventoryList();
             $('#edit-inventory-modal').modal('toggle');
             $.notify("Inventory update successful for product: " + JSON.parse(json).barcode,"success");
      },
      error: handleAjaxError
   });

   return false;
}


function getInventoryList(){
   var url = getInventoryUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayInventoryList(data);
      },
      error: handleAjaxError
   });
}

var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
   var file = $('#inventoryFile')[0].files[0];
   if(!file)
   {
       handleErrorNotification("Please select a file!")
       return;
   }
   readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
   fileData = results.data;
   var meta = results.meta;
   if(meta.fields.length!=2 ) {
       var row = {};
       row.error="Number of headers do not match!";
       errorData.push(row);
       updateUploadDialog()
       return;
   }
   if(meta.fields[0]!="barcode" || meta.fields[1]!="quantity")
   {
       var row = {};
       row.error="Incorrect headers name!";
       errorData.push(row);
       updateUploadDialog()
       return;
   }
   const MAX_ROWS = 5000
   if(results.data.length>MAX_ROWS){
       handleErrorNotification("File too big!");
       return
   }
   uploadRows();
}

function uploadRows(){
   updateUploadDialog();
   if(processCount==fileData.length){
      getInventoryList()
      return;
   }

   var row = fileData[processCount];
   barcode = row.barcode
   processCount++;
   if(row.__parsed_extra){
   	    row.error="extra fields present!"
   	    row.error_in_row_no = processCount
           errorData.push(row);
           uploadRows();
   	}
   	else
   	{

   var json = JSON.stringify(row);
   var url = getInventoryUrl() + '/' + barcode

   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             uploadRows();
      },
      error: function(response){
             var data = JSON.parse(response.responseText);
             row.error=data["message"];
             row.error_in_row_no = processCount
             errorData.push(row);
             uploadRows();

      }
   });
   }

}

function downloadErrors(){
   writeFileData(errorData);
}

function displayInventoryList(data){
   var $tbody = $('#inventory-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      if(getRole()==="supervisor")
      {
          var buttonHtml = ' <button type="button" title="Edit Inventory" class="btn btn-outline-secondary" onclick="displayEditInventory(\'' + e.barcode + '\')"><i class="fas fa-edit fa-xs"></i></button>'
          var row = '<tr class="text-center">'
          + '<td>' + e.barcode + '</td>'
          + '<td>' + e.productName + '</td>'
          + '<td>'  + e.quantity + '</td>'
          + '<td>' + buttonHtml + '</td>'
          + '</tr>';
            $tbody.append(row);
      }
      else
      {
        var row = '<tr class="text-center">'
                  + '<td>' + e.barcode + '</td>'
                  + '<td>' + e.productName + '</td>'
                  + '<td>'  + e.quantity + '</td>'
                  + '</tr>';
                    $tbody.append(row);
      }

   }
}

function displayEditInventory(barcode){
   var url = getInventoryUrl() + "/" + barcode;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayInventory(data);
      },
      error: handleAjaxError
   });
}

function resetUploadDialog(){
   var $file = $('#inventoryFile');
   $file.val('');
   $('#inventoryFileName').html("Choose File");
   processCount = 0;
   fileData = [];
   errorData = [];
   updateUploadDialog();
}

function updateUploadDialog(){
   $('#rowCount').html("" + fileData.length);
   $('#processCount').html("" + processCount);
   $('#errorCount').html("" + errorData.length);
   if(errorData.length>0)
     $('#download-errors').show()
   else
    $('#download-errors').hide()

}

function updateFileName(){
   var $file = $('#inventoryFile');
   var fileName = $file.val();
   $('#inventoryFileName').html(fileName);
   fileData = [];
   errorData = [];
   processCount = 0;
   updateUploadDialog()
}

function displayUploadData(){
   resetUploadDialog();
   $('#upload-inventory-modal').modal('toggle');

}

function displayInventory(data){
   $("#inventory-edit-form input[name=barcode]").val(data.barcode);
   $("#inventory-edit-form input[name=quantity]").val(data.quantity);
   $('#edit-inventory-modal').modal('toggle');

}


//INITIALIZATION CODE
function init(){
   $('#inventory-edit-form').submit(updateInventory);
   $('#refresh-data').click(getInventoryList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
    $('#inventory-link').addClass('active').css("border-bottom","2px solid black")
}

$(document).ready(init);
$(document).ready(getInventoryList);