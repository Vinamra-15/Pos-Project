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
  ajaxCall(url,"GET",{},(data)=>displayOrderList(data))
}

function getProductByBarcode(barcode, onSuccess) {
  const url = getProductUrl() + "?barcode=" + barcode;
  ajaxCall(url,"GET",{},(data)=>onSuccess(data))
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
    handleErrorNotification("Please enter barcode!")
    return
  }
  const index = orderItems.findIndex((it) => it.barcode === item.barcode);
  if (index == -1) {
    orderItems.push(item);
  } else {
    totalSellingPrice = orderItems[index].quantity*orderItems[index].sellingPrice + Number.parseFloat(item.sellingPrice)*Number.parseInt(item.quantity)
    orderItems[index].quantity += Number.parseInt(item.quantity);
    orderItems[index].sellingPrice = (totalSellingPrice/orderItems[index].quantity).toFixed(2)

  }
}

function addOrderItemToAddModal(event) {
  event.preventDefault()
  const item = getCurrentOrderItem('add');
    getProductByBarcode(item.barcode, (product) => {
        if(product.mrp<item.sellingPrice){
          handleErrorNotification("Selling Price should not be greater than MRP: "+product.mrp);
          return;
        }
        addItem({
          barcode: product.barcode,
          name: product.name,
          sellingPrice: item.sellingPrice.toFixed(2),
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
                  handleErrorNotification("Selling Price should not be greater than MRP: "+product.mrp);
                  return;
                }
        addItem({
          barcode: product.barcode,
          name: product.name,
          sellingPrice: item.sellingPrice.toFixed(2),
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
    $('#place-order-btn').hide()
    $('#cancel-place-order-btn').hide()

  }
  else{
    $('#create-order-table').show()
    $('#place-order-btn').show()
    $('#cancel-place-order-btn').show()
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

  const row = `
          <tr>
              <td colspan="4" style="text-align: right"><strong>Bill Amount : </strong></td>

            <td id="grandTotal"><strong>&#8377 ${numberWithCommas(calculateBillAmount().toFixed(2))}</strong></td>
          </tr>
        `;
    $tbody.append(row);
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
    ajaxCall(url,"GET",{},(data)=>{
      let items = data.items
      let amount = 0;
      for(let i in items){
        amount = amount + items[i].sellingPrice*items[i].quantity
      }
      return amount
    })
}

function convertDate(datetime){
    let date = new Date(datetime).toString();
    return date.split(' G')[0];
}

 function displayOrderList(orders) {
  $('#numberOfResults').text("Showing " + orders.length + " results :")
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
                <button title="View Order" type="button" class="btn btn-outline-info" onclick="fetchOrderDetails(${order.id},'add')">
                  <i class="fa fa-info-circle" aria-hidden="true"></i>
                </button>

                <button title="Edit Order" type="button" class="btn btn-outline-warning" onclick="editOrderDetails(${order.id})">
                                  <i class="fas fa-edit fa-xs"></i>
                                </button>
                <button title="Download Invoice" type="button" class="btn btn-outline-danger downloadInvoiceBtn" id="invoiceBtn-${order.id}" onclick="downloadInvoice(${order.id})">
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
  ajaxCall(url,"GET",{},(data)=>{
       orderItems = data.items;
       if(typeOfOperation==='add')
       displayOrderDetails(data);
       else
       {
            $("#edit-item-form input[name=id]").val(data.id)
            const $heading = $('#update-modal-heading')
            $heading.text("Update Order (Order Id: " + data.id + " )")
            displayEditOrderItems(orderItems);
            displayEditOrderModal()
       }
  })
}
function displayEditOrderModal(){
    $('#edit-order-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
    $('#inputEditModalBarcode').val("");
    $('#inputEditModalQuantity').val("");
    $('#inputEditModalSellingPrice').val("");
}

function calculateBillAmount(){
    let grandTotal = 0
    for(let i in orderItems){
        let item = orderItems[i]
        grandTotal+=(Number.parseInt(item.quantity)*Number.parseFloat(item.sellingPrice))
    }
   return grandTotal;

}
function displayEditOrderItems(data){

      const $tbody = $('#edit-order-table').find('tbody');
      $tbody.empty();
      if(data.length===0){
          $('#edit-order-table').hide()
          $('#update-order-btn').hide()
          $('#cancel-update-order-btn').hide()
        }
      else
      {
        $('#edit-order-table').show()
        $('#update-order-btn').show()
        $('#cancel-update-order-btn').show()
      }

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

        const row = `
                    <tr>
                        <td colspan="4" style="text-align: right"><strong>Bill Amount : </strong></td>

                      <td id="grandTotal"><strong>&#8377 ${numberWithCommas(calculateBillAmount().toFixed(2))}</strong></td>
                    </tr>
                  `;
            $tbody.append(row);


}
function onQuantityChanged(barcode,event) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newQuantity = event.target.value
  orderItems[index].quantity = Number.parseInt(newQuantity);
  $('#grandTotal').html(`<strong>&#8377 ${numberWithCommas(calculateBillAmount().toFixed(2))}</strong>`)
}
function onSellingPriceChanged(barcode,event){
    const index = orderItems.findIndex((it) => it.barcode === barcode);
      if (index == -1) return;
      const newSellingPrice = event.target.value
      orderItems[index].sellingPrice = Number.parseFloat(newSellingPrice);
      $('#grandTotal').html(`<strong>&#8377 ${numberWithCommas(calculateBillAmount().toFixed(2))}</strong>`)
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
      handleErrorNotification("Please add some orders!");
      return;
  }
  const json = JSON.stringify(data);
  const url = getOrderUrl();
  ajaxCall(url,"POST",json,()=>{
    onSuccess()
    $.notify("Order placed successfully!","success");
  })
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
        handleErrorNotification("Please add some orders!");
        return;
      }

      const json = JSON.stringify(data);
      const url = getOrderUrl() + id;
      ajaxCall(url,"PUT",json,()=>{
        hideEditingModal()
        $.notify("Order-"+id+" updated successfully!","success");
      })
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
  $('#orders-link').addClass('active').css("border-bottom","0.125rem solid black")
}

$(document).ready(init);
$(document).ready(getOrderList);