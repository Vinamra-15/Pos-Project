function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}

function addUser(event){
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();
	ajaxCall(url,"POST",json,getUserList)
	return false;
}

function getUserList(){
	var url = getUserUrl();
	ajaxCall(url,"GET",{},(data)=>displayUserList(data))
}

function displayUserList(data){
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteUser(' + e.id + ')">delete</button>'
		buttonHtml += ' <button onclick="displayEditUser(' + e.id + ')">edit</button>'
		var row = '<tr class="text-center">'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
}

$(document).ready(init);
$(document).ready(getUserList);

