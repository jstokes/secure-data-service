class Staff
  include MongoMapper::Document

  key :_id, BSON::ObjectId
  key :type, String

  has_one :body, :as => :entity
  has_one :metadata, :as => :meta

  connection Mongo::Connection.new('localhost')
  set_database_name 'sli'
  set_collection_name 'staff'

  def last_comma_first
      self.body.name.lastSurname + ", " + self.body.name.firstName + " (Staff)"
  end

  def get_id
      UUIDTools::UUID.parse_raw(self._id.to_s)
  end

  def staffUniqueStateId
    self.body.staffUniqueStateId
  end

  def roles
    ['IT Administrator']
  end
end
