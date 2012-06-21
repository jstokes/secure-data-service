$(document).ready(function() {
	if (is_sandbox) {
  var buttonEnableForCustom = function() {
    if($("#custom_ed_org").val().length == 0) {
      $("#provisionButton").attr("disabled","disabled")
    }
    else {
      $("#provisionButton").removeAttr("disabled")
    }
  }
  
  $("#use_rsa").click(function() {
   alert("hit");
   $("#rsaKeyField").toggle(this.checked);
   $("#rsaKeyField").val("");
  });
  
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
