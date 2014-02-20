# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/
table = null

jQuery ->
  table = $('#simple-table').dataTable(
    "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    "bLengthChange": false,
    "bFilter": false,
    "bRetrieve": true,
    "bPaginate": false,
    "aoColumnDefs": [ 
       { "bSortable": false, "aTargets": [ 0 ] }
    ],
    "aaSorting": [[ 1, "asc" ], [2, "asc"], [3, "asc"]]
    )
  $('#simple-table tbody tr').click ->
    details = $(@)
    console.log details
    if table.fnIsOpen(@)
      table.fnClose(@)
      $(@).find("td:first-child.expandable").toggleClass("expanded")
    else
      table.fnOpen(@, details.find('td.hidden').html(), "details")
      $(@).find("td:first-child.expandable").toggleClass("expanded")

jQuery ->
  if $('.pagination').length
    $(window).scroll ->
      url = $('div.pagination #paginate-next').attr('href')
      if url && $(window).scrollTop() > $(document).height() - $(window).height() - 50
        $('div.pagination').text("Fetching more results...")
        $.getScript(url)
    $(window).scroll()