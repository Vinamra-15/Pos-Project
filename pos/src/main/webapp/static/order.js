function getBaseUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl
}
function getOrderUrl(){
	return getBaseUrl() + "/api/orders/";
}
function getProductUrl(){
	return getBaseUrl() + "/api/products";
}


function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      displayOrderList(data);
    },
    error: handleAjaxError,
  });
}

function getProductByBarcode(barcode, onSuccess) {
  const url = getProductUrl() + "?barcode=" + barcode;
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      onSuccess(data);
    },
    error: handleAjaxError,
  });
}

//UI DISPLAY METHODS
let orderItems = [];

function getCurrentOrderItem(typeOfOperation) {
   if(typeOfOperation==='add')
  {
    return {
        barcode: $('#inputAddModalBarcode').val(),
        quantity: Number.parseInt($('#inputAddModalQuantity').val()),
        sellingPrice:Number.parseFloat($('#inputAddModalSellingPrice').val())
    };
  }
  else
  {
    return {
        barcode: $('#inputEditModalBarcode').val(),
        quantity: Number.parseInt($('#inputEditModalQuantity').val()),
        sellingPrice:Number.parseFloat($('#inputEditModalSellingPrice').val())
      };
  }
}

function addItem(item) {
  if(item.barcode==null){
    alert("Please enter barcode!")
    return
  }
  const index = orderItems.findIndex((it) => it.barcode === item.barcode);
  if (index == -1) {
    orderItems.push(item);
  } else {
    orderItems[index].quantity += item.quantity;
  }
}

function addOrderItem(typeOfOperation) {
  const item = getCurrentOrderItem(typeOfOperation);
  if(typeOfOperation==='add')
  {
    getProductByBarcode(item.barcode, (product) => {
        addItem({
          barcode: product.barcode,
          name: product.name,
          sellingPrice: item.sellingPrice,
          quantity: item.quantity,
        })
         displayCreateOrderItems(orderItems);
         resetAddItemForm();
        }

        )
    }
    else
    {
        getProductByBarcode(item.barcode, (product) => {
            addItem({
              barcode: product.barcode,
              name: product.name,
              sellingPrice: item.sellingPrice,
              quantity: item.quantity,
            })
            displayEditOrderItems(orderItems)
            resetEditItemForm();
            });
    }

}


function displayCreateOrderItems(data) {
  const $tbody = $('#create-order-table').find('tbody');
  $tbody.empty();

  for (let i in data) {
    const item = data[i];
    const row = `
      <tr class="text-center">
        <td class="barcodeData">${item.barcode}</td>
        <td>${item.name}</td>
        <td >${item.sellingPrice}</td>
        <td>
          <input
            id="order-item-${item.barcode}"
            type="number"
            class="form-control"
            value="${item.quantity}"
            onchange="onQuantityChanged('${item.barcode}',event)"
            style="width:70%" min="1">
        </td>
        <td>
          <button onclick="deleteOrderItem('${item.barcode}','add')" class="btn btn-outline-danger">Delete</button>
        </td>
      </tr>
    `;

    $tbody.append(row);
  }
}

function deleteOrderItem(barcode,typeOfOperation) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  orderItems.splice(index, 1);
  if(typeOfOperation==='edit')
  {
    displayEditOrderItems(orderItems);
  }
  else
  displayCreateOrderItems(orderItems)
}

function resetAddItemForm() {
  $('#inputAddModalBarcode').val('');
  $('#inputAddModalQuantity').val('');
  $('#inputAddModalSellingPrice').val('');
}

function resetEditItemForm() {
  $('#inputEditModalBarcode').val('');
  $('#inputEditModalQuantity').val('');
  $('#inputEditModalSellingPrice').val('');
}

function resetCreateModal() {
  resetAddItemForm();
  orderItems = [];
  displayCreateOrderItems(orderItems);
}

//function getBillAmount(id){
//    const url = getOrderUrl + id;
//    $.ajax({
//        url: url,
//        type: 'GET',
//        success: function (data) {
//          // sum data items
//          let amount = 0;
//          for(let i in data.items){
//            amount = amount + i.sellingPrice
//          }
//          return amount
//        },
//        error: handleAjaxError,
//      });
//
//}


function displayOrderList(orders) {
  var $tbody = $('#order-table').find('tbody');
  $tbody.empty();

  orders.forEach((order) => {
    //billAmount = getBillAmount(order.id)
//    let invoicePath = order.invoicePath.toString().split('.')[0].split('/')[1]

    var row = `
        <tr class="text-center">
            <td>${order.id}</td>
            <td>${order.datetime}</td>
            <td>
                <button type="button" class="btn btn-outline-secondary" onclick="fetchOrderDetails(${order.id},'add')">
                  Details
                </button>

                <button type="button" class="btn btn-outline-secondary" onclick="editOrderDetails(${order.id})">
                                  Edit
                                </button>
                <button type="button" class="btn btn-outline-secondary downloadInvoiceBtn" id="invoiceBtn-${order.id}" onclick="downloadInvoice(${order.id})">
                                                  Invoice
                                                </button>
            </td>
        </tr>
    `;
    $tbody.append(row);
  });




}
//function saveByteArray(reportName, byte) {
//    var blob = new Blob([byte], {type: "application/pdf"});
//    var link = document.createElement('a');
//    link.href = window.URL.createObjectURL(blob);
//    var fileName = reportName;
//    link.download = fileName;
//    link.click();
//};
//function base64ToArrayBuffer(base64) {
//    var binaryString = window.atob(base64);
//    var binaryLen = binaryString.length;
//    var bytes = new Uint8Array(binaryLen);
//    for (var i = 0; i < binaryLen; i++) {
//       var ascii = binaryString.charCodeAt(i);
//       bytes[i] = ascii;
//    }
//    return bytes;
// }

function downloadInvoice(orderId) {
    let req = new XMLHttpRequest();
    req.open("GET", `/pos/download/invoice/${orderId}`, true);
    req.responseType = "blob";
//    console.log(invoicePath)

    req.onreadystatechange = () => { // Call a function when the state changes.

          if (req.readyState === XMLHttpRequest.DONE && req.status === 200) {

          let blob = req.response;
//          console.log(blob.size);
          let link=document.createElement('a');
          link.href=window.URL.createObjectURL(blob);
          link.download=`${orderId}.pdf`;
          link.click();
          $.notify("Invoice Generated for order: " + orderId,"success");
            // Request finished. Do processing here.
          }
          else
          {
            $.notify("Invoice Generation in progress!","warning");
          }

        }
        req.send()

//    req.onload = function (event) {
//      let blob = req.response;
//      console.log(blob.size);
//      let link=document.createElement('a');
//      link.href=window.URL.createObjectURL(blob);
//      link.download=`${invoicePath}.pdf`;
//      link.click();
//    };
//    req.onerror = function(event){
//        handleAjaxError();
//    }


//    const url = `/pos/download/invoice/${invoicePath}`
//        $.ajax({
//            url: url,
//            type: 'GET',
//            success: function (data) {
//                console.log(data)
//            var bytes = new Uint8Array(data); // pass your byte response to this constructor
//
//            var blob=new Blob([bytes], {type: "application/pdf"});// change resultByte to bytes
//
//            var link=document.createElement('a');
//            link.href=window.URL.createObjectURL(blob);
//            link.download="myFileName.pdf";
//            link.click();
////                var sampleArr = base64ToArrayBuffer(data);
////                saveByteArray("Sample Report", sampleArr);
//              // sum data items
////              data.responseType = "blob"
////              let blob = data.response
////
////              let link=document.createElement('a');
////              link.href=window.URL.createObjectURL(data);
////              link.download=`${invoicePath}.pdf`;
////              link.click();
//            },
//            error: handleAjaxError,
//          });


}

function fetchOrderDetails(id,typeOfOperation) {
  var url = getOrderUrl() + id;
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
        orderItems = data.items;
       if(typeOfOperation==='add')
       displayOrderDetails(data);
       else
       {
            $("#edit-item-form input[name=id]").val(data.id)
            displayEditOrderItems(orderItems);
            displayEditOrderModal()
       }
    },
    error: handleAjaxError,
  });
}
function displayEditOrderModal(){
    $('#edit-order-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}
function displayEditOrderItems(data){

      const $tbody = $('#edit-order-table').find('tbody');
      $tbody.empty();

      for (var i in data) {
          var item = data[i];


          const row = `
            <tr class="text-center">
              <td class="barcodeData">${item.barcode}</td>
              <td>${item.name}</td>
              <td >${item.sellingPrice}</td>
              <td>
                <input
                    id="order-item-${item.barcode}"
                    type="number"
                    class="form-control"
                    value = "${item.quantity}"
                    onchange="onQuantityChanged('${item.barcode}',event)"
                    style="width:70%" min="1">
              </td>
              <td>
                   <button onclick="deleteOrderItem('${item.barcode}','edit')" class="btn btn-outline-danger">Delete</button>
              </td>

            </tr>
          `;

          $tbody.append(row);


        }
}

function onQuantityChanged(barcode,event) {
//    console.log(event)
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;

//$(`#order-item-${barcode}`)
  const newQuantity = event.target.value
  orderItems[index].quantity = Number.parseInt(newQuantity);
}




function resetUploadDialog() {
  //Reset file name
  var $file = $('#orderFile');
  $file.val('');
  $('#orderFileName').html('Choose File');
  //Reset various counts
  processCount = 0;
  fileData = [];
  errorData = [];
  //Update counts
  updateUploadDialog();
}

function updateUploadDialog() {
  $('#rowCount').html('' + fileData.length);
  $('#processCount').html('' + processCount);
  $('#errorCount').html('' + errorData.length);
}

function updateFileName() {
  var $file = $('#orderFile');
  var fileName = $file.val();
  $('#orderFileName').html(fileName);
}

function displayUploadData() {
  resetUploadDialog();
  $('#upload-order-modal').modal('toggle');
}

function displayOrderDetails(data) {
  const datetime = data.datetime
  displayOrderDetailsModal();
  const $time = $('#date-time')
  $time.text("Order placed on: " + datetime)
  const $id = $('#order-id')
  $id.text("Order Id: " + data.id)
  const $tbody = $('#order-details-table').find('tbody');
  $tbody.empty();
  const items = data.items;
  let billAmount = 0;
    for (let i in items) {
      const item = items[i];
      billAmount+=item.sellingPrice*item.quantity;
      const row = `
        <tr class="text-center">
          <td>${item.barcode}</td>
          <td>${item.name}</td>
          <td >${item.sellingPrice}</td>
          <td>${item.quantity}</td>
        </tr>
      `;

      $tbody.append(row);
    }
    const row = `
            <tr>
                <td>Bill Amount: </td>
                <td></td>
               <td></td>
              <td>${billAmount}</td>
            </tr>
          `;
    $tbody.append(row);

}

function displayOrderDetailsModal(){
    $('#order-details-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}

function hideOrderDetailsModal() {
  $('#order-details-modal').modal('toggle');
  getOrderList();
}


function displayCreationModal() {
  resetCreateModal();
  $('#create-order-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}

function hideCreationModal() {
  $('#create-order-modal').modal('toggle');
  getOrderList();
}

function hideEditingModal() {
  $('#edit-order-modal').modal('toggle');
  getOrderList();
}

//function registerUpdateButton(id){
//$('#update-order-btn').click(()=> updateOrder(id))
//}

function editOrderDetails(id){
//    registerUpdateButton(id)
    fetchOrderDetails(id,'edit');

}

//INITIALIZATION CODE
function init() {
  $('#add-order-item').click(addOrderItem);
  $('#create-order').click(displayCreationModal);
  $('#refresh-data').click(getOrderList);
  $('#upload-data').click(displayUploadData);
  $('#place-order-btn').click(placeNewOrder);
  $('#update-order-btn').click(updateOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);

// Place Order
function placeNewOrder() {
  const data = orderItems.map((it) => {
    return {
      barcode: it.barcode,
      quantity: it.quantity,
      sellingPrice:it.sellingPrice
    };
  });

  const json = JSON.stringify(data);
  placeOrder(json, hideCreationModal);
}

//BUTTON ACTIONS
function placeOrder(json, onSuccess) {
  //Set the values to update
  const url = getOrderUrl();

  $.ajax({
    url: url,
    type: 'POST',
    data: json,
    headers: {
      'Content-Type': 'application/json',
    },
    success: function(){
        onSuccess()
        $.notify("Order placed successfully!","success");
    },
    error: handleAjaxError,
  });

  return false;
}

function updateOrder(){
    const id = $("#edit-item-form input[name=id]").val();
    const data = orderItems.map((it) => {
        return {
          barcode: it.barcode,
          quantity: it.quantity,
          sellingPrice:it.sellingPrice
        };
      });

      const json = JSON.stringify(data);
      const url = getOrderUrl() + id;

        $.ajax({
          url: url,
          type: 'PUT',
          data: json,
          headers: {
            'Content-Type': 'application/json',
          },
          success: function(){
            hideEditingModal()
            $.notify("Order Update successful!","success");
          },
          error: handleAjaxError,
        });
//        $('#update-order-btn').unbind('click')
        return false;

}

