$(document).ready(function() {
  $("#addKeyBtn").click(function() {
    alert("pause");
    $("#rsaKeyField").toggle();
//    $("#rsaKeyField").val(""); // this doesn't work, want to clear the text whenever shown/hidden
  });
	if (is_sandbox) {
  var buttonEnableForCustom = function() {
    if($("#custom_ed_org").val().length == 0) {
      $("#provisionButton").attr("disabled","disabled")
    }
    else {
      $("#provisionButton").removeAttr("disabled")
    }
  }
  
  $("input[type=radio][id!=custom]").click(function() {
   $("#custom_ed_org").attr("disabled","disabled");
   $("#provisionButton").removeAttr("disabled")});
  $("#custom").click(function() {
   $("#custom_ed_org").removeAttr("disabled");
   buttonEnableForCustom()});
  $("#custom_ed_org").bind('input propertychange', buttonEnableForCustom); 
  if($("#custom").attr("type") == "radio"){
    $("#custom_ed_org").attr("disabled","disabled")
  }
  $("#provisionButton").attr("disabled", "disabled")
  }
})
