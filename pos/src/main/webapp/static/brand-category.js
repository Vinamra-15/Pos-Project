
function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}

//BUTTON ACTIONS
function addBrandCategory(event){
    event.preventDefault()
	//Set the values to update
	var $form = $("#brand-category-form");
	var json = toJson($form);
	var url = getBrandCategoryUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandCategoryList();
	   		$.notify(JSON.parse(json).brand + " in category: "+JSON.parse(json).category + " added successfully!","success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrandCategory(event){
    event.preventDefault()

	//Get the ID
	var id = $("#brand-category-edit-form input[name=id]").val();
	var url = getBrandCategoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-category-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandCategoryList();
	   		$('#edit-brand-category-modal').modal('toggle');
	   		$.notify("Edit successful!","success");

	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandCategoryList(){
	var url = getBrandCategoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandCategoryList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteBrandCategory(id){
	var url = getBrandCategoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandCategoryList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandCategoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
    const MAX_ROWS = 5000
    if(results.data.length>MAX_ROWS){
        $.notify("File too big!","error");
        return
    }
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    getBrandCategoryList()
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	var json = JSON.stringify(row);
	var url = getBrandCategoryUrl();
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
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});



}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandCategoryList(data){
	var $tbody = $('#brand-category-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-outline-secondary" onclick="displayEditBrandCategory(' + e.id + ')"><i class="fas fa-edit fa-xs"></i></button>'
		if(getRole()==="supervisor")
		{var row = '<tr class="text-center">'
		+ '<td class="text-center">' + e.brand + '</td>'
		+ '<td class="text-center">'  + e.category + '</td>'
		+ '<td class="text-center">' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);}
        else{
            var row = '<tr class="text-center">'
            		+ '<td class="text-center">' + e.brand + '</td>'
            		+ '<td class="text-center">'  + e.category + '</td>'
            		+ '</tr>';
            $tbody.append(row);
        }
	}
}

function displayEditBrandCategory(id){
	let url = getBrandCategoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandCategory(data);
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandCategoryFile');
	$file.val('');
	$('#brandCategoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandCategoryFile');
	var fileName = $file.val();
	$('#brandCategoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-category-modal').modal('toggle');
}

function displayBrandCategory(data){
	$("#brand-category-edit-form input[name=brand]").val(data.brand);
	$("#brand-category-edit-form input[name=category]").val(data.category);
	$("#brand-category-edit-form input[name=id]").val(data.id);
	$('#edit-brand-category-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-brand-category').submit(addBrandCategory);
	$('#brand-category-edit-form').submit(updateBrandCategory);
	$('#refresh-data').click(getBrandCategoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
    $('#brandCategoryFile').on('change',function(){
        var fileName = $(this).val();
        $('#brandCategoryFileName').html(fileName);
    });
    $('#brands-link').addClass('active').css("border-bottom","2px solid black")
}

$(document).ready(init);
$(document).ready(getBrandCategoryList);

