def post_entity(endpoint)
  puts "token: " + @sessionId
  puts @expected_entity.to_json.to_s
  restHttpPost("/v1/#{endpoint}", @expected_entity.to_json, 'application/vnd.slc+json')
  assert(@res.code == 201, "#{@res.code} - Could not create entity #{@expected_entity.to_json}.")
  @location = @res.raw_headers['location'][0]
  puts "location: " + @location
  @id = @location.split("/").last
  puts "id: " + @id
  restHttpGetAbs(@location, 'application/vnd.slc+json')
  assert(@res.code == 200, "#{@res.code} - Could not fetch newly created entity #{@expected_entity.to_json}.")
end

def get_entity
  restHttpGetAbs(@location, 'application/vnd.slc+json')
  assert(@res.code == 200, "#{@res.code} - Could not fetch entity #{@expected_entity.to_json}.")
  actual = JSON.parse @res
  actual.delete('id')
  actual.delete('links')
  actual.delete('entityType')
  assert(actual.eql?(@expected_entity),"GET contents different to that expected #{actual.to_json}.")
end

def unsupported_put(endpoint)
  restHttpPut("/v1/#{endpoint}/#{@id}", @expected_entity.to_json, 'application/vnd.slc+json')
  assert(@res.code == 405, "Unexpected HTTP code returned: #{@res.code}.")
end

def unsupported_patch(endpoint)
  restHttpPatch("/v1/#{endpoint}/#{@id}", Hash.new.to_json, 'application/vnd.slc+json')
  assert(@res.code == 405, "Unexpected HTTP code returned: #{@res.code}.")
end

def deleteEntity(endpoint)
  restHttpDelete("/v1/#{endpoint}/#{@id}", 'application/vnd.slc+json')
  assert(@res.code == 204, "Unexpected HTTP code returned: #{@res.code}.")
end

def post_custom_entity(endpoint)
  @expected_custom_entity = {
                              "some field" => "some value",
                              "some other field" => 44
                            }
  restHttpPost("/v1/#{endpoint}/#{@id}/custom", @expected_custom_entity.to_json, 'application/vnd.slc+json')
  assert(@res.code == 201, "#{@res.code} - Could not create entity #{@expected_custom_entity.to_json}")
end

def get_custom_entity(endpoint)
  restHttpGet("/v1/#{endpoint}/#{@id}/custom", 'application/vnd.slc+json')
  assert(@res.code == 200, "#{@res.code} - Could not fetch entity #{@expected_custom_entity.to_json}")
  actual = JSON.parse @res
  assert(actual.eql?(@expected_custom_entity),"GET contents different to that expected #{actual.to_json}")
end

def put_custom_entity(endpoint)
  @expected_custom_entity = {
                              "some field" => "some other value",
                              "additional field" => 666
                            }
  restHttpPut("/v1/#{endpoint}/#{@id}/custom", @expected_custom_entity.to_json, 'application/vnd.slc+json')
  assert(@res.code == 204, "#{@res.code} - Could not update custom entity #{@expected_custom_entity.to_json}")
end

def delete_custom_entity(endpoint)
  restHttpDelete("/v1/#{endpoint}/#{@id}/custom", 'application/vnd.slc+json')
  assert(@res.code == 204, "#{@res.code} - Could not delete custom entity #{@expected_custom_entity.to_json}")
end

def get_deleted_custom_entity(endpoint)
  restHttpGet("/v1/#{endpoint}/#{@id}/custom", 'application/vnd.slc+json')
  assert(@res.code == 404, "Unexpected HTTP code returned: #{@res.code}")
end

Then /^I GET the deleted entity/ do
  restHttpGetAbs(@location, 'application/vnd.slc+json')
  assert(@res.code == 404, "Unexpected HTTP code returned: #{@res.code}.")
end

def put_entity(endpoint)
  restHttpPut("/v1/#{endpoint}/#{@id}", @expected_entity.to_json, 'application/vnd.slc+json')
  assert(@res.code == 204, "#{@res.code} - Could not update entity #{@expected_entity.to_json}")
end

def patch_entity(endpoint)
 restHttpPatch("/v1/#{endpoint}/#{@id}", @expected_patch_entity.to_json, 'application/vnd.slc+json')
 assert(@res.code == 204, "#{@res.code} - Could not update entity with PATCH: #{@expected_patch_entity.to_json}")
end

def patch_custom_entity(endpoint)
   restHttpPatch("/v1/#{endpoint}/#{@id}/custom", Hash.new.to_json, 'application/vnd.slc+json')
   assert(@res.code == 405, "Unexpected HTTP code returned: #{@res.code}.")
end



