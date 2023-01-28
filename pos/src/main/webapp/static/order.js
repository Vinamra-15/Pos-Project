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
  if(item.barcode===""){
    $.notify("Please enter barcode!","error")
    return
  }
  const index = orderItems.findIndex((it) => it.barcode === item.barcode);
  if (index == -1) {
    orderItems.push(item);
  } else {
    orderItems[index].quantity += item.quantity;
  }
}

function addOrderItemToAddModal(event) {
  event.preventDefault()
  const item = getCurrentOrderItem('add');
    getProductByBarcode(item.barcode, (product) => {
        if(product.mrp<item.sellingPrice){
          $.notify("Selling Price should not be greater than MRP: "+product.mrp,"error");
          return;
        }
        addItem({
          barcode: product.barcode,
          name: product.name,
          sellingPrice: item.sellingPrice,
          quantity: item.quantity,
        })
         displayCreateOrderItems(orderItems);
         resetAddItemForm();
        })
}

function addOrderItemToEditModal(event) {
    event.preventDefault()
    const item = getCurrentOrderItem('edit');
    getProductByBarcode(item.barcode, (product) => {
        if(product.mrp<item.sellingPrice){
                  $.notify("Selling Price should not be greater than MRP: "+product.mrp,"error");
                  return;
                }
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

function displayCreateOrderItems(data) {
  const $tbody = $('#create-order-table').find('tbody');
  $tbody.empty();
  if(data.length===0){
    $('#create-order-table').hide()
  }
  else{
    $('#create-order-table').show()
  }
  for (let i in data) {
    const item = data[i];
    const row = `
      <tr class="text-center">
        <td class="barcodeData">${item.barcode}</td>
        <td>${item.name}</td>
        <td style="display:flex; justify-content:center;">
            <input
                id="order-item-${item.barcode}-sellingPrice"
                type="number"
                class="form-control"
                value="${item.sellingPrice}"
                onchange="onSellingPriceChanged('${item.barcode}',event)"
                style="width:90%" min="0"
                step="0.01"
                required/>

        </td>
        <td>
          <input
            id="order-item-${item.barcode}"
            type="number"
            class="form-control"
            value="${item.quantity}"
            onchange="onQuantityChanged('${item.barcode}',event)"
            style="width:90%" min="1" required/>
        </td>
        <td>
          <button onclick="deleteOrderItem('${item.barcode}','add')" class="btn btn-outline-danger"><i class="fa fa-trash" aria-hidden="true"></i></button>
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

function getBillAmount(id){
    const url = getOrderUrl() + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
          // sum data items
          let items = data.items
          let amount = 0;
          for(let i in items){
            amount = amount + items[i].sellingPrice*items[i].quantity
          }
          return amount
        },
        error: handleAjaxError,
      });
}

function convertDate(datetime){
    let date = new Date(datetime).toString();
    return date.split(' G')[0];
}

 function displayOrderList(orders) {
  var $tbody = $('#order-table').find('tbody');
  $tbody.empty();

  for(let i in orders){
    let order = orders[i]
    let time = convertDate(order.datetime)
    var row = `
        <tr class="text-center">
            <td>${order.id}</td>
            <td>${time}</td>
            <td>
                <button type="button" class="btn btn-outline-secondary" onclick="fetchOrderDetails(${order.id},'add')">
                  <i class="fa fa-info-circle" aria-hidden="true"></i>
                </button>

                <button type="button" class="btn btn-outline-secondary" onclick="editOrderDetails(${order.id})">
                                  <i class="fas fa-edit fa-xs"></i>
                                </button>
                <button type="button" class="btn btn-outline-secondary downloadInvoiceBtn" id="invoiceBtn-${order.id}" onclick="downloadInvoice(${order.id})">
                                                  <i class="fa fa-file-pdf-o" aria-hidden="true"></i></button>
            </td>
        </tr>
    `;
    $tbody.append(row);
  };
}

function downloadInvoice(orderId) {
    let req = new XMLHttpRequest();
    req.open("GET", `/pos/download/invoice/${orderId}`, true);
    req.responseType = "blob";
    req.onreadystatechange = () => {
          if (req.readyState === XMLHttpRequest.DONE && req.status === 200) {
          let blob = req.response;
          let link=document.createElement('a');
          link.href=window.URL.createObjectURL(blob);
          link.download=`${orderId}.pdf`;
          link.click();
          $.notify("Invoice Generated for order: " + orderId,"success");
          }
        }
        req.send()
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
              <td style="display:flex; justify-content:center;">
                <input
                    id="order-item-${item.barcode}-sellingPrice"
                    type="number"
                    class="form-control"
                    value="${item.sellingPrice}"
                    onchange="onSellingPriceChanged('${item.barcode}',event)"
                    style="width:90%" min="0"
                    step="0.01" required/>
              </td>
              <td>
                <input
                    id="order-item-${item.barcode}"
                    type="number"
                    class="form-control"
                    value = "${item.quantity}"
                    onchange="onQuantityChanged('${item.barcode}',event)"
                    style="width:90%" min="1" required/>
              </td>
              <td>
                   <button onclick="deleteOrderItem('${item.barcode}','edit')" class="btn btn-outline-danger"><i class="fa fa-trash" aria-hidden="true"></i></button>
              </td>

            </tr>
          `;
          $tbody.append(row);
        }
}
function onQuantityChanged(barcode,event) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newQuantity = event.target.value
  orderItems[index].quantity = Number.parseInt(newQuantity);
}
function onSellingPriceChanged(barcode,event){
    const index = orderItems.findIndex((it) => it.barcode === barcode);
      if (index == -1) return;
      const newSellingPrice = event.target.value
      orderItems[index].sellingPrice = Number.parseFloat(newSellingPrice);
}
function resetUploadDialog() {
  var $file = $('#orderFile');
  $file.val('');
  $('#orderFileName').html('Choose File');
  processCount = 0;
  fileData = [];
  errorData = [];
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
  const datetime = (new Date(data.datetime)).toString()
  displayOrderDetailsModal();
  const $time = $('#date-time')
  $time.text(datetime.split('G')[0]).wrapInner("<strong />");
  const $id = $('#order-details-heading')
  $id.text("Order Id: " + data.id)
  const $tbody = $('#order-details-table').find('tbody');
  $tbody.empty();
  const items = data.items;
  let billAmount = 0.0;
    for (let i in items) {
      const item = items[i];
      billAmount+=item.sellingPrice*item.quantity;
      const row = `
        <tr class="text-center">
          <td>${item.barcode}</td>
          <td>${item.name}</td>
          <td >${numberWithCommas(item.sellingPrice.toFixed(2))}</td>
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
              <td><strong>&#8377 ${numberWithCommas(billAmount.toFixed(2))}</strong></td>
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
function editOrderDetails(id){
    fetchOrderDetails(id,'edit');
}
// Place Order
function placeNewOrder(event) {
    event.preventDefault()
  const data = orderItems.map((it) => {
    return {
      barcode: it.barcode,
      quantity: it.quantity,
      sellingPrice:it.sellingPrice
    };
  });
  placeOrder(data, hideCreationModal);
}
function placeOrder(data, onSuccess) {
  if(data.length===0){
          $.notify("Please add some orders!","error");
          return;
  }
  const json = JSON.stringify(data);
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

function updateOrder(event){
    event.preventDefault()
    const id = $("#edit-item-form input[name=id]").val();
    const data = orderItems.map((it) => {
        return {
          barcode: it.barcode,
          quantity: it.quantity,
          sellingPrice:it.sellingPrice
        };
      });
      if(data.length===0){
        $.notify("Please add some orders!","error");
        return;
      }

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
            $.notify("Order-"+id+" updated successfully!","success");
          },
          error: handleAjaxError,
        });
        return false;

}

//INITIALIZATION CODE
function init() {
  $('#add-item-form-add-modal').submit(addOrderItemToAddModal)
  $('#edit-item-form').submit(addOrderItemToEditModal)
  $('#create-order').click(displayCreationModal);
  $('#refresh-data').click(getOrderList);
  $('#upload-data').click(displayUploadData);
  $('#update-order-form').submit(updateOrder);
  $('#create-order-form').submit(placeNewOrder)
  $('#orders-link').addClass('active').css("border-bottom","2px solid black")
}

$(document).ready(init);
$(document).ready(getOrderList);