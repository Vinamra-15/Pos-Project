
function getBrandCategoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}

function addBrandCategory(event){
    event.preventDefault()
	var $form = $("#brand-category-add-form");
	var json = toJson($form);
	var url = getBrandCategoryUrl();
	ajaxCall(url,"POST",json,response=>{
	    getBrandCategoryList();
        	   		resetBrandCategoryModal()
        	   		$.notify(JSON.parse(json).brand + " in category: "+JSON.parse(json).category + " added successfully!","success");
	})
	return false;
}

function updateBrandCategory(event){
    event.preventDefault()
	var id = $("#brand-category-edit-form input[name=id]").val();
	var url = getBrandCategoryUrl() + "/" + id;
	var $form = $("#brand-category-edit-form");
	var json = toJson($form);
	ajaxCall(url,"PUT",json,response=>{
	    getBrandCategoryList();
        $('#edit-brand-category-modal').modal('toggle');
        $.notify("Edit successful!","success");
	})
	return false;
}


function getBrandCategoryList(){
	var url = getBrandCategoryUrl();
	ajaxCall(url,"GET",{},(data)=>displayBrandCategoryList(data))
}


var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandCategoryFile')[0].files[0];
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
    if(meta.fields[0]!="brand" || meta.fields[1]!="category")
    {
        var row = {};
        row.error="Incorrect headers name!";
        errorData.push(row);
        updateUploadDialog()
        return;
    }
    const MAX_ROWS = 5000
    if(results.data.length>MAX_ROWS){
        handleErrorNotification("File too big!")
        return
    }
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
	if(processCount==fileData.length){
	    getBrandCategoryList()
		return;
	}
	var row = fileData[processCount];
	processCount++;
	if(row.__parsed_extra){
	    row.error="extra fields present!"
	    row.error_in_row_no = processCount
        errorData.push(row);
        uploadRows();
	}
	else{
	var json = JSON.stringify(row);
	var url = getBrandCategoryUrl();
	ajaxCall(url,"POST",json,uploadRows,(response)=>{
	    var data = JSON.parse(response.responseText);
        row.error=data["message"];
        row.error_in_row_no = processCount
        errorData.push(row);
        uploadRows();
	})
	}
}

function downloadErrors(){
	writeFileData(errorData);
}

function displayBrandCategoryList(data){
    $('#numberOfResults').text("Showing " + data.length + " results :")
	var $tbody = $('#brand-category-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button title="Edit Brand Category" type="button" class="btn btn-outline-secondary" onclick="displayEditBrandCategory(' + e.id + ')"><i class="fas fa-edit fa-xs"></i></button>'
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

function displayAddBrandCategoryModal(){
    $('#add-brand-category-modal').modal('toggle');
}

function resetBrandCategoryModal(){
    $("#brand-category-add-form input[name=brand]").val("");
    $("#brand-category-add-form input[name=category]").val("");
    $("#brand-category-add-form input[name=id]").val("");
    $('#add-brand-category-modal').modal('toggle');
}

function displayEditBrandCategory(id){
	let url = getBrandCategoryUrl() + "/" + id;
	ajaxCall(url,"GET",{},(data)=>displayBrandCategory(data))
}

function resetUploadDialog(){
	var $file = $('#brandCategoryFile');
	$file.val('');
	$('#brandCategoryFileName').html("Choose File");
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
	var $file = $('#brandCategoryFile');
	var fileName = $file.val();
	$('#brandCategoryFileName').html(fileName);
	fileData = [];
    errorData = [];
    processCount = 0;
    updateUploadDialog()
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
	$('#brand-category-add-form').submit(addBrandCategory);
	$('#brand-category-edit-form').submit(updateBrandCategory);
	$('#refresh-data').click(getBrandCategoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#addBrandCategoryBtn').click(displayAddBrandCategoryModal)
    $('#brandCategoryFile').on('change', updateFileName)
    $('#brands-link').addClass('active').css("border-bottom","0.125rem solid black")
}

$(document).ready(init);
$(document).ready(getBrandCategoryList);

