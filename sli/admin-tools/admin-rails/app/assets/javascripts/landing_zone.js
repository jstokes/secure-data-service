$(document).ready(function() {
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
  $("#custom_ed_org").keyup(buttonEnableForCustom); })
