defaultRights = ["READ_GENERAL", "WRITE_GENERAL", "READ_RESTRICTED", "WRITE_RESTRICTED", "AGGREGATE_READ", "AGGREGATE_WRITE", "READ_PUBLIC", "ADMIN_APPS"]

jQuery ->
  unless initCustomRoleScripts?
    return

  populateTable(roles)

  $("#addRightUi button").click ->
    option = $('#addRightUi option:selected')
    if (option.val() == 'none')
      return
    text = option.text()
    right = createLabel('right', text)
    right = wrapInputWithDeleteButton(right, "span", text)
    $("#addRightUi").parent().append(right)
    $("#addRightUi").parent().append(" ")
    populateRightComboBox($(@).parents("tr"))
    enableSaveButtonIfPossible($(@).parents("tr"))

  #Wire up Add Role button
  $("#addGroupButton").click ->
    newRow = $("<tr><td><div class='groupTitle'></div></td><td></td><td></td><td></td></tr>")
    $("#custom_roles tbody").append(newRow)

    newRow.find("td:eq(3)").append($("#rowEditTool").clone().children())

    # Disable the save button until they've added a role and right
    newRow.find(".rowEditToolSaveButton").addClass("disabled") #disable until we get ajax success
    newRow.find(".rowEditToolSaveButton").attr('disabled', 'disabled')
    editRow(newRow)

    #Add row edit tool
    wireEditButtons(newRow)
    

  #Wire up reset to defaults
  $("#resetToDefaultsButton").click ->
    if (confirm("Resetting to default roles will remove any existing role mapping and will restore the default roles.  This operation cannot be undone.  Are you sure you want to reset?"))
      populateTable(default_roles)
      saveData(getJsonData())

  $("#addRoleUi button").click ->
    tr = $(@).parents("tr")
    td = tr.find("td:eq(0)")
    roleName = $("#addRoleUi input").val().trim()
    if (roleName == "")
      return

    #Check for duplicates
    if (getAllRoles().indexOf(roleName) > -1)
      return alert("The role name " + roleName + " is already used.")
    div = createLabel('role', roleName)
    div = wrapInputWithDeleteButton(div, "div", roleName)
    div.wrap("<div/>")
    td.append(div.parent())
    $("#addRoleUi input").val("")
    enableSaveButtonIfPossible(tr)
  
enableSaveButtonIfPossible = (tr)  ->
  roles = getRoles(tr)  
  rights = getRights(tr)  
  if roles.length > 0 && rights.length > 0
    tr.find(".rowEditToolSaveButton").removeClass("disabled")
    tr.find(".rowEditToolSaveButton").removeAttr('disabled')
    

createLabel = (type, name) ->
  label = $('#labelUi').clone()
  label.find("span").text(name)
  label.children().addClass(type)
  return label.children()

editRow = (tr) ->
  
  #hide all the other edit buttons
  curRowButtons = tr.find(".rowButtons")
  tr.parent().find(".rowButtons").each ->
    if $(@) != curRowButtons
      $(@).hide()
 
  $("#addGroupButton").addClass("disabled")
  $("#addGroupButton").attr('disabled', 'disabled')
  tr.find(".saveButtons").show()
  tr.find(".editButtons").hide()

  populateRightComboBox(tr)
  tr.find("td:eq(1)").prepend($("#addRightUi"))
  $("#addRightUi").fadeIn()

  #Give it a nice glow
  tr.find("td").addClass("highlight")

  #Add role name field
  td = tr.find("td:eq(0)")
  addRoleUi = $("#addRoleUi")
  td.prepend(addRoleUi)
  addRoleUi.find("input").val("")
  addRoleUi.fadeIn()

  #Turn group name into input
  groupName = tr.find("td:eq(0)").find(".groupTitle")
  input = $("<input type='text' id='editInput' class='groupTitle' />").val(groupName.text().trim())

  if (groupName.text().trim() == "")
    input.attr("placeholder", "Enter group name")
    input.attr("id", "groupName")
  groupName.replaceWith(input)
  groupName = tr.find("td:eq(0)").find(".groupTitle")
  td.prepend(groupName)
  td.prepend("Group Name:")

  #Add delete button to each role name
  tr.find("td:eq(0) .roleLabel").each -> wrapInputWithDeleteButton($(@), "div", groupName)
  tr.find("td:eq(1) .roleLabel").each -> wrapInputWithDeleteButton($(@), "span", groupName)

populateRightComboBox = (tr) ->
  console.log(tr)
  #Add right combobox - only add rights that haven't already been used
  curRights = getRights(tr)
  $("#addRightUi option").each ->
    if ($(@).val() != "none")
      $(@).remove()

  for right in defaultRights
    if (curRights.indexOf(right) < 0)
      $("#addRightUi select").append($("<option></option>").val(right).text(right))


wrapInputWithDeleteButton = (input, type, name) ->
  div = $('<span>').addClass("input-append")
  button = $("<button class='btn' id='DELETE_" + input.text() + "' >&times;</button>")
  div.append(button)
  button.click ->
    label = button.parent().parent().find('.editable')
    console.log(label.parents("tr"))
    if label.hasClass('right')
      rights = getRights(label.parents("tr"))
      if rights.length <= 1
        return alert("Role group must contain at least one right.")

    if label.hasClass('role')
      roles = getRoles(label.parents("tr"))
      if roles.length <= 1
        return alert("Role group must contain at least one role.")
      
    button.parent().parent().fadeOut -> 
      populateRightComboBox($(this).parents("tr"))
      $(this).remove()
  
  input.addClass("editable")
  input.wrap("<" + type + "/>").parent().css("white-space", "nowrap")
  input.parent().append(div)
  return input.parent()

editRowStop = (tr) ->
  
  #Reshow all the row buttons
  #tr.parent().find(".rowButtons").each ->
  #  $(@).show()

  $("#addGroupButton").removeClass("disabled")
  $("#addGroupButton").removeAttr('disabled')
  $("#rowEditToolSaveButton").addClass("disabled") #disable until we get ajax success
  $("#rowEditToolSaveButton").attr('disabled', 'disabled')

  #Move the components back to their original location
  $("#addRoleUi").hide()
  $("#components").append($("#addRoleUi"))
  $("#addRightUi").hide()
  $("#components").append($("#addRightUi"))
  td = tr.find("td")
  td.removeClass("highlight")
    
  #Replace input with delete buttons back to normal inputs
  td.find(".role ").each ->
    $(@).parent().replaceWith(createLabel('role', $(@).text()))
  td.find(".right").each ->
    $(@).parent().replaceWith(createLabel('right', $(@).text()))

  #Replace editable group name with normal div
  input = tr.find("td:eq(0) input")
  div = $("<div class='groupTitle'></div>").text(input.val())
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
      $("#rowEditToolSaveButton").removeAttr('disabled')
      $(".saveButtons").hide()
      window.location.reload(true);
    error: (data, status, xhr) ->
      console.log("error", data, status, xhr)
      window.location.reload(true);


getJsonData = () ->
  data = []
  $("#custom_roles tr:gt(0)").each ->
    groupName = $(@).find("td:eq(0)").find(".groupTitle").val()
    if groupName == ""
      groupName = $(@).find("td:eq(0)").find(".groupTitle").text()

    foo = $(@).find("td:eq(0)")
    console.log(foo)

    roles = []
    $(@).find("td:eq(0) .customLabel").each ->
      roles.push($(@).text())
    rights = []
    $(@).find("td:eq(1) .customLabel").each ->
      rights.push($(@).text())
    data.push({"groupTitle": groupName, "names": roles, "rights": rights})
  console.log(data)
  return data

getRights = (tr) ->
    rights = []
    tr.find("td:eq(1) .customLabel").each ->
      rights.push($(@).text())
    console.log(rights)
    return rights

getRoles = (tr) ->
    roles = []
    tr.find("td:eq(0) .customLabel").each ->
      roles.push($(@).text())
    console.log(roles)
    return roles

getAllRoles = () ->
  roles = []
  $("#custom_roles tr:gt(0)").each ->
    $(@).find("td:eq(0) .customLabel").each ->
      roles.push($(@).text())
  return roles

populateTable = (data) ->
  $("#custom_roles tbody").children().remove()
  for role in data
    newRow = $("<tr><td><div></div></td><td></td><td></td><td></td></tr>")
    $("#custom_roles tbody").append(newRow)

    newRow.find("td:eq(0)").append($("<div class='groupTitle'></div>").text(role.groupTitle))

    for name in role.names
      div = $('<div/>')
      div.append(createLabel('role', name))
      newRow.find("td:eq(0)").append(div)

    for right in role.rights
      newRow.find("td:eq(1)").append(createLabel('right', right))
      newRow.find("td:eq(1)").append(" ")

    newRow.find("td:eq(3)").append($("#rowEditTool").clone().children())
    wireEditButtons(newRow)

wireEditButtons = (tr) ->
  tr.find(".rowEditToolDeleteButton").click ->
    if (confirm("Do you really want to delete the role group"))
      $(@).parents("tr").remove()
      saveData(getJsonData())

  tr.find(".rowEditToolEditButton").click ->
    editRow($(@).parents("tr"))

  tr.find(".rowEditToolCancelButton").click ->
      window.location.reload(true);

  tr.find(".rowEditToolSaveButton").click ->
      editRowStop(tr)      


