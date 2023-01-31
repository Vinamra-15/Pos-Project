
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

brandCategoryData = []
brandSet = new Set()
categorySet = new Set()
function getBrandCategory(){
     $.ajax({
           url: getBrandUrl(),
           type: 'GET',
           success: function(response) {
                brandCategoryData = response
                for(let i in response){
                    brandSet.add(response[i].brand)
                    categorySet.add(response[i].category)
                }

                populateBrandCategoryDropDown(brandSet,categorySet,"add")
                populateBrandCategoryDropDown(brandSet,categorySet,"edit")



           },
           error: handleAjaxError
        });
}
function populateBrandCategoryDropDown(brandSet,categorySet,typeOfOperation){
    $("#select-brand-"+typeOfOperation).empty()
    $("#select-category-"+typeOfOperation).empty()
    $("#select-brand-"+typeOfOperation).append('<option selected="" value="">Select Brand</option>')
    $("#select-category-"+typeOfOperation).append('<option selected="" value="">Select Category</option>')
    brandSet.forEach(function(brand){
        let brandOptionHTML = '<option value="'+ brand +'">'+ brand+'</option>'
        $("#select-brand-"+typeOfOperation).append(brandOptionHTML)
    })
    categorySet.forEach(function(category){
         let categoryOptionHTML = '<option value="'+ category +'">'+ category+'</option>'
         $("#select-category-"+typeOfOperation).append(categoryOptionHTML)
    })

}

let primary = ""; //defines which of the Brand Category option was selected first

function brandChanged(event){
    const typeOfOperation = event.data.typeOfOperation
    if(primary===""||primary==="brand"){
    primary="brand"
    let brand = event.target.value
    if(brand==="")
    {
        populateBrandCategoryDropDown(brandSet,categorySet,typeOfOperation)
        primary=""
        return
    }
    $("#select-category-"+typeOfOperation).empty()
    $("#select-category-"+typeOfOperation).append('<option selected="" value="">Select Category</option>')
    for(let i in brandCategoryData){
        if(brandCategoryData[i].brand===brand){
            let categoryOptionHTML = '<option value="'+ brandCategoryData[i].category +'">'+ brandCategoryData[i].category+'</option>'
                                 $("#select-category-"+typeOfOperation).append(categoryOptionHTML)
        }
    }
    }
}


function categoryChanged(event){
    const typeOfOperation = event.data.typeOfOperation
    if(primary===""||primary==="category"){
    primary = "category"
    let category = event.target.value
    if(category===""){
        populateBrandCategoryDropDown(brandSet,categorySet,typeOfOperation)
        primary=""
        return
    }
    $("#select-brand-"+typeOfOperation).empty()
    $("#select-brand-"+typeOfOperation).append('<option selected="" value="">Select Brand</option>')
    for(let i in brandCategoryData){
        if(brandCategoryData[i].category===category){
            let brandOptionHTML = '<option value="'+ brandCategoryData[i].brand +'">'+ brandCategoryData[i].brand+'</option>'
                                 $("#select-brand-"+typeOfOperation).append(brandOptionHTML)
        }
    }
  }
}



function getData(json){
    let data = JSON.parse(json)
    let dataToPost = {
        barcode:data.barcode,
        name:data.name,
        brand:data.brand,
        category:data.category,
        mrp:data.mrp
    }
    return dataToPost
}

function addProduct(event){
	event.preventDefault()
	var $form = $("#product-add-form");
	var json = toJson($form);
	var url = getProductUrl();
	var data = getData(json)
	if(!data.brand){
        handleErrorNotification("Please select brand!")
        return;
    }
	if(!data.category){
	    handleErrorNotification("Please select category!")
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
            $("#product-add-form select[name=brand]").prop('selectedIndex',0);
            $("#product-add-form select[name=category]").prop('selectedIndex',0);;
            $("#product-add-form input[name=id]").val("");
	   		$("#add-product-modal").modal('toggle');
	   		$.notify("Product added successfully!","success");
	   		resetBrandCategoryDropDown();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
    event.preventDefault()
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;

	var $form = $("#product-edit-form");
	var json = toJson($form);
	var data = getData(json)
	if(!data.brand){
        handleErrorNotification("Please select brand!")
        return;
    }
    if(!data.category){
        handleErrorNotification("Please select category!")
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
	   		resetBrandCategoryDropDown()
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
	updateUploadDialog();
	if(processCount==fileData.length){
	    getProductList()
		return;
	}
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



function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button title="Edit Product" type="button" class="btn btn-outline-secondary"  onclick="displayEditProduct(' + e.id + ')"><i class="fas fa-edit fa-xs"></i></button>'
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

function resetUploadDialog(){
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");

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
	$("#product-edit-form select[name=brand]").val(data.brand).change();
	$("#product-edit-form select[name=category]").val(data.category).change();
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
    $('#select-brand-add').change({typeOfOperation:"add"},brandChanged)
    $('#select-category-add').change({typeOfOperation:"add"},categoryChanged)
    $('#select-brand-edit').change({typeOfOperation:"edit"},brandChanged)
    $('#select-category-edit').change({typeOfOperation:"edit"},categoryChanged)
    $('#add-modal-close').click(resetBrandCategoryDropDown)
    $('#edit-modal-close').click(resetBrandCategoryDropDown)
    $('#cancel-add-btn').click(resetBrandCategoryDropDown)
    $('#products-link').addClass('active').css("border-bottom","2px solid black")
    $('#edit-product-modal').on('hidden.bs.modal', function () {
      primary="";
      resetBrandCategoryDropDown()
    })

}
function resetBrandCategoryDropDown(){
    populateBrandCategoryDropDown(brandSet,categorySet,"add")
    $('#select-brand-add').prop('selectedIndex',0);
    $('#select-category-add').prop('selectedIndex',0);
    primary = ""

}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandCategory);

