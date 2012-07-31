lastRow = -1 #last highlighted row
editRowIndex = -1 #last row that was clicked on
defaultRights = ["READ_GENERAL", "WRITE_GENERAL", "READ_RESTRICTED", "WRITE_RESTRICTED", "AGGREGATE_READ", "AGGREGATE_WRITE"]

jQuery ->
  populateTable(roles)

  $(".rowEditTool").mouseenter ->
    $(".rowEditTool").show()

  $("#rowEditToolEditButton").click ->
    if (editRowIndex < 0)
      editRow(lastRow)

  $("#rowEditToolCancelButton").click ->
      window.location.reload(true);

  $("#rowEditToolDeleteButton").click ->
    if (confirm("Do you really want to delete the role group"))
      $("#custom_roles tr:eq(" + lastRow + ")").remove()
      $(".rowEditTool").hide()
      saveData(getJsonData())

  $("#rowEditToolSaveButton").click ->
    if (editRowIndex > -1)
      editRowStop()      

  $("#addRightUi button").click ->
    option = $('#addRightUi option:selected')
    if (option.val() == 'none')
      return
    text = option.text()
    right = createLabel('right', text)
    right = wrapInputWithDeleteButton(right, "span")
    $("#addRightUi").parent().append(right)
    $("#addRightUi").parent().append(" ")
    populateRightComboBox()

  #Wire up Add Role button
  $("#addGroupButton").click ->
    newRow = $("<tr><td><div></div></td><td></td><td></td></tr>")
    $("#custom_roles tbody").append(newRow)
    newRow.mouseenter -> rowMouseEnter(newRow)
    lastRow = newRow.parent().children().index(newRow) + 1
    editRowIndex = lastRow
    drawEditBox(newRow)
    editRow(lastRow)

  #Wire up reset to defaults
  $("#resetToDefaultsButton").click ->
    if (confirm("Resetting to defaults will delete any existing role mappings and cannot be undone.  Are you sure you want to reset?"))
      populateTable(default_roles)
      saveData(getJsonData())

  $("#addRoleUi button").click ->
    td = $("#custom_roles tr:eq(" + editRowIndex + ") td:eq(1)")
    roleName = $("#addRoleUi input").val().trim()
    if (roleName == "")
      return

    #Check for duplicates
    if (getAllRoles().indexOf(roleName) > -1)
      return alert("The role name " + roleName + " is already used.")
    div = createLabel('role', roleName)
    div = wrapInputWithDeleteButton(div, "div")
    div.wrap("<div/>")
    td.append(div.parent())
    $("#addRoleUi input").val("")
  
rowMouseEnter = (row) ->   
  if (editRowIndex < 0)
    drawEditBox(row)

drawEditBox = (row) ->
  lastRow = row.parent().children().index(row) + 1
  xPos = row.position().left + row.width()
  yPos = row.position().top
  $(".rowEditTool").show()
  #$(".rowEditTool").height(row.height())
  $(".rowEditTool").offset({top: yPos, left: xPos})

createLabel = (type, name) ->
  label = $('#labelUi').clone()
  label.find("span").text(name)
  label.children().addClass(type)
  return label.children()

editRow = (rowNum) ->
  $("#addGroupButton").addClass("disabled")
  $(".editButtons").hide()
  $(".saveButtons").show()
  editRowIndex = rowNum

  populateRightComboBox()
  $("#custom_roles tr:eq(" + rowNum + ") td:eq(2)").prepend($("#addRightUi"))
  $("#addRightUi").fadeIn()

  #Give it a nice glow
  $("#custom_roles tr:eq(" + rowNum + ") td").addClass("highlight")

  #Add role name field
  td = $("#custom_roles tr:eq(" + rowNum + ") td:eq(1)")
  addRoleUi = $("#addRoleUi")
  td.prepend(addRoleUi)
  addRoleUi.find("input").val("")
  addRoleUi.fadeIn()

  #Turn group name into input
  groupName = $("#custom_roles tr:eq(" + rowNum + ") td:eq(0) div")
  input = $("<input type='text'/>").val(groupName.text().trim())
  if (groupName.text().trim() == "")
    input.attr("placeholder", "Enter group name")
    input.attr("id", "groupName")
  groupName.replaceWith(input)
   
  #Add delete button to each role name
  $("#custom_roles tr:eq(" + rowNum + ") td:eq(1) .roleLabel").each -> wrapInputWithDeleteButton($(@), "div")
  $("#custom_roles tr:eq(" + rowNum + ") td:eq(2) .roleLabel").each -> wrapInputWithDeleteButton($(@), "span")

populateRightComboBox = () ->
  #Add right combobox - only add rights that haven't already been used
  curRights = getRights(editRowIndex)
  $("#addRightUi option").each ->
    if ($(@).val() != "none")
      $(@).remove()

  for right in defaultRights
    if (curRights.indexOf(right) < 0)
      $("#addRightUi select").append($("<option></option>").val(right).text(right))


wrapInputWithDeleteButton = (input, type) ->
  div = $('<span>').addClass("input-append")
  button = $("<button class='btn'>&times;</button>")
  div.append(button)
  button.click ->
    label = button.parent().parent().find('.editable')
    if label.hasClass('right')
      rights = getRights(editRowIndex)
      if rights.length <= 1
        return alert("Role group must contain at least one right.")

    if label.hasClass('role')
      roles = getRoles(editRowIndex)
      if roles.length <= 1
        return alert("Role group must contain at least one role.")
      
    button.parent().parent().fadeOut -> 
      $(this).remove()
      populateRightComboBox()
  
  input.addClass("editable")
  input.wrap("<" + type + "/>").parent().css("white-space", "nowrap")
  input.parent().append(div)
  return input.parent()

editRowStop = () ->
  $("#addGroupButton").removeClass("disabled")
  $("#rowEditToolSaveButton").addClass("disabled") #disable until we get ajax success

  #Move the components back to their original location
  $("#addRoleUi").hide()
  $("#components").append($("#addRoleUi"))
  $("#addRightUi").hide()
  $("#components").append($("#addRightUi"))
  td = $("#custom_roles tr:eq(" + editRowIndex + ") td")
  td.removeClass("highlight")
    
  #Replace input with delete buttons back to normal inputs
  td.find(".role ").each ->
    $(@).parent().replaceWith(createLabel('role', $(@).text()))
  td.find(".right").each ->
    $(@).parent().replaceWith(createLabel('right', $(@).text()))

  #Replace editable group name with normal div
  input = $("#custom_roles tr:eq(" + editRowIndex + ") td:eq(0) input")
  div = $("<div/>").text(input.val())
  input.replaceWith(div)
  saveData(getJsonData()) 
  editRowIndex = -1

saveData = (json) ->
  #Remove any errors from the last save
  $(".error").remove()
  $.ajax UPDATE_URL,
    type: 'PUT'
    contentType: 'application/json'
    data: JSON.stringify({json})
    dataType: 'json'
    success: (data, status, xhr) ->
      $("#rowEditToolSaveButton").removeClass("disabled")
      $(".editButtons").show()
      $(".saveButtons").hide()
    error: (data, status, xhr) ->
      console.log("error", data, status, xhr)
      window.location.reload(true);


getJsonData = () ->
  data = []
  $("#custom_roles tr:gt(0)").each ->
    groupName = $(@).find("td:eq(0) div").text()
    roles = []
    $(@).find("td:eq(1) .customLabel").each ->
      roles.push($(@).text())
    rights = []
    $(@).find("td:eq(2) .customLabel").each ->
      rights.push($(@).text())
    data.push({"groupTitle": groupName, "names": roles, "rights": rights})
  return data

getRights = (row) ->
    rights = []
    $("#custom_roles tr:eq(" + row + ")").find("td:eq(2) .customLabel").each ->
      rights.push($(@).text())
    return rights

getRoles = (row) ->
    roles = []
    $("#custom_roles tr:eq(" + row + ")").find("td:eq(1) .customLabel").each ->
      roles.push($(@).text())
    return roles

getAllRoles = () ->
  roles = []
  $("#custom_roles tr:gt(0)").each ->
    $(@).find("td:eq(1) .customLabel").each ->
      roles.push($(@).text())
  return roles

populateTable = (data) ->
  $("#custom_roles tbody").children().remove()
  for role in data 
    newRow = $("<tr><td><div></div></td><td></td><td></td></tr>")
    newRow.mouseenter -> rowMouseEnter(newRow)
    $("#custom_roles tbody").append(newRow)

    newRow.find("td:eq(0)").append($("<div></div>").text(role.groupTitle))

    for name in role.names
      div = $('<div/>')
      div.append(createLabel('role', name))
      newRow.find("td:eq(1)").append(div)

    for right in role.rights
      newRow.find("td:eq(2)").append(createLabel('right', right))
      newRow.find("td:eq(2)").append(" ")

  $("#custom_roles tr:gt(0)").mouseenter -> rowMouseEnter($(@))

  $("#custom_roles tr").mouseleave ->
    if (editRowIndex < 0)
      $(".rowEditTool").hide()

