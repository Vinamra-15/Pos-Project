
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getBrandCategory(){
    $.ajax({
    	   url: getBrandUrl(),
    	   type: 'GET',
    	   success: function(response) {
    	        for(let i in response){
    	            let optionHTML = '<option value="'+ response[i].brand + "~" + response[i].category+'">'+ response[i].brand + " - " + response[i].category+'</option>'
    	            $('#select-brand-category-add').append(optionHTML)
    	            $('#select-brand-category-edit').append(optionHTML)
    	        }
    	   },
    	   error: handleAjaxError
    	});
}



function getData(json){
    let data = JSON.parse(json)
    let dataToPost = {
        barcode:data.barcode,
        name:data.name,
        brand:data.brandCategory.split('~')[0],
        category:data.brandCategory.split('~')[1],
        mrp:data.mrp
    }
    return dataToPost
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	event.preventDefault()

	var $form = $("#product-add-form");
	var json = toJson($form);
	var url = getProductUrl();
	var data = getData(json)
	if(!data.category){
	    handleErrorNotification("Please select brand category!")
	    return;
	}
	data = JSON.stringify(data)

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	        $("#product-add-form input[name=name]").val("");
            $("#product-add-form input[name=barcode]").val("");
            $("#product-add-form input[name=mrp]").val("");
            $("#product-add-form select[name=brandCategory]").prop('selectedIndex',0);;
            $("#product-add-form input[name=id]").val("");
	   		$("#add-product-modal").modal('toggle');
	   		$.notify("Product added successfully!","success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
    event.preventDefault()

	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);
	var data = getData(json)
    	if(!data.category){
    	    handleErrorNotification("Please select brand category!")
    	    return;
    	}
    	data = JSON.stringify(data)

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   		$('#edit-product-modal').modal('toggle');
	   		$.notify("Product update successful for product: " + JSON.parse(json).barcode,"success");
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	if(!file)
    {
        handleErrorNotification("Please select a file!");
        return;
    }
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	var meta = results.meta;
    if(meta.fields.length!=5 ) {
        var row = {};
        row.error="Number of headers do not match!";
        errorData.push(row);
        updateUploadDialog()
        return;
    }
    if(meta.fields[0]!="name" || meta.fields[1]!="brand" || meta.fields[2]!="category" || meta.fields[3]!="barcode"|| meta.fields[4]!="mrp")
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
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    getProductList()
		return;
	}
	//Process next row
	var row = fileData[processCount];
	processCount++;
	if(row.__parsed_extra){
    	    row.error="extra fields!"
    	    row.error_in_row_no = processCount
            errorData.push(row);
            uploadRows();
    }
    else
    {
        var json = JSON.stringify(row);
        var url = getProductUrl();

        //Make ajax call
        $.ajax({
           url: url,
           type: 'POST',
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

//UI DISPLAY METHODS

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-outline-secondary"  onclick="displayEditProduct(' + e.id + ')"><i class="fas fa-edit fa-xs"></i></button>'
		if(getRole()==="supervisor")
        {
            var row = '<tr class="text-center">'
            + '<td>' + e.barcode + '</td>'
            + '<td>' + e.name + '</td>'
            + '<td>' + e.brand + '</td>'
            + '<td>'  + e.category + '</td>'
            + '<td>' + numberWithCommas( e.mrp.toFixed(2)) + '</td>'
            + '<td>' + buttonHtml + '</td>'
            + '</tr>';
            $tbody.append(row);
        }
        else
        {
            var row = '<tr class="text-center">'
                        + '<td>' + e.barcode + '</td>'
                        + '<td>' + e.name + '</td>'
                        + '<td>' + e.brand + '</td>'
                        + '<td>'  + e.category + '</td>'
                        + '<td>' + numberWithCommas( e.mrp.toFixed(2))  + '</td>'
                        + '</tr>';
            $tbody.append(row);
        }
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});	
}

//function handleAjaxError(e){
//    $.notify("Cannot perform operation!","error");
//}

function resetUploadDialog(){
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
	fileData = [];
    errorData = [];
    processCount = 0;
    updateUploadDialog()
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form select[name=brandCategory]").val(data.brand + "~" + data.category);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function displayAddProductModal(){
    $('#add-product-modal').modal('toggle');
}




//INITIALIZATION CODE
function init(){
	$('#product-add-form').submit(addProduct);
	$('#product-edit-form').submit(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
    $('#products-link').addClass('active').css("border-bottom","2px solid black")
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandCategory);


