module AppsHelper
  def sortable(title, value = nil)
    value ||= title.downcase
    direction = params[:direction] == "ascending" ? "descending" : "ascending"
    link_to title, {:sort => value, :direction => direction}, {:class => direction}
  end

  #def get_district_hierarchy
  #foo = EducationOrganization.find(:all, :params => {"shortNameOfInstitution" => "NYPDSS"})
  #
  #  ed_orgs = EducationOrganization.all
  #  result = {}
  #
  #  ed_orgs.each do |ed_org|
  #
  #  end
  #end

  def get_district_hierarchy
    result = {}
    ed_orgs = EducationOrganization.all
    ed_org_assocs = EducationOrganizationAssociations.all
    ed_org_assocs.each do |assoc|
      parent_id = assoc.educationOrganizationParentId
      child_id = assoc.educationOrganizationChildId
      current_parent = {"id" => parent_id, "name" => get_ed_org_name_from_id(ed_orgs, parent_id)}
      current_child = {"id" => child_id, "name" => get_ed_org_name_from_id(ed_orgs, child_id)}

      if result.keys.include?(current_parent)
        result[current_parent].push(current_child)
      else
        result[current_parent] = [current_child]
      end
    end
    return result
  end

  def get_ed_org_name_from_id(ed_orgs, id)
    ed_orgs.select{|ed_org| ed_org.id == id}[0].nameOfInstitution
  end

  # For client_id and client_secret, return 'Pending' if app isn't yet registered
  def client_field_value(app, field)
    if app.attributes.has_key? 'registration' and app.registration.status == 'APPROVED'
      app.attributes[field]
    else
      "Pending"
    end
  end
end
