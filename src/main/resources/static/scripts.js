/* Resuable HTML */
$(document).ready(function(){
    $('#header-main').load("common.html #header-main"); 
    $('#header').load("common.html #header"); 
    $('#header-logo').load("common.html #header-logo"); 
    $('#header-search').load("common.html #header-search");
    $('#r-nav').load("common.html .r-nav");
    $('#user-info').load("common.html .user-info");
    $('#guest-info').load("common.html .guest-info");
    $('#footer').load("common.html #footer");
    $('#footer-grey').load("common.html #footer-grey");
});

/* Header */
function showCart(){
    $("#cart").toggle(150);
}

function showUserInfo(){
    $("#user-info").toggle(150);
    $("#guest-info").toggle(150);
}

/* Search */
function doSearch(){
    if($("#txtSearchLocation").val() !== ""){
        if($("#txtSearchKeyword").val() !== "") doSearchKeyword();
        else doSearchLocation();
    }
}

function doSearchLocation(){
    var test = 'search?location=' + $("#txtSearchLocation").val();
    window.location.href = test;
}

function doSearchKeyword(){
    var test = 'search?location=' + $("#txtSearchLocation").val() + "&keyword=" + $("#txtSearchKeyword").val();
    window.location.href = test;
}

/* Submit */
function submitForm(){
    var valid = true;
    $('input[type="text"]').each(function(){
        if($(this).val() == "") valid = false;
    });
    if(valid == false){
        $("#invalid").text("Must fill out all fields");
        $("#invalid").show();
    }
    else{ $('form').submit(); }
}

function submitRestaurantInfo(){
    var valid = true;
    if($("txtName").val() == "" || $("#txtStreet").val() == "" || $("#txtCity").val() == "" || $("#txtState").val() == "" || $("#txtZipcode").val() == ""){
        $("#invalid").text("Must fill out required fields");
        $("#invalid").show();
    }
    else{ $('form').submit(); }
}

function submitMenuItem(){
    var valid = true;
    if($("txtName").val() == "" || $("#txtCategory").val() == ""){
        $("#invalid").text("Must fill out required fields");
        $("#invalid").show();
    }
    else{ $('form').submit(); }
}

function submitOrderItem(){
    if ($("input[type=radio]:checked").length > 0) {
        $('form').submit();
   }
   else{
       $("#invalid").text("Please select an option");
       $("#invalid").show();
   }
}

function submitOption(){
    if($("#txtOption").val() == "" || $("#txtPrice").val() == ""){
        $("#invalid").text("Must fill out required fields");
        $("#invalid").show();
    }
    else if(isNaN($("#txtPrice").val()) == true){
        $("#invalid").text("Invalid price");
    }
    else{ $('form').submit(); }
}

/* etc */
function goBackOnce(){
    history.go(-1);
}