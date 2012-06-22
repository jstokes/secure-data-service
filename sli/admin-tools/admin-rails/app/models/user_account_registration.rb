class UserAccountRegistration

  include ActiveModel::Validations
  include ActiveModel::Conversion
  extend ActiveModel::Naming

  attr_accessor :email, :firstName, :lastName, :password, :vendor

  validates_presence_of :firstName, :lastName, :email, :password, :password_confirmation
  validates :email, :email => true
  validates :password, :confirmation => true #password_confirmation attr
  validates_presence_of :vendor unless APP_CONFIG["is_sandbox"]

  def initialize(attributes = {})
    attributes.each do |name, value|
      send("#{name}=", value)
    end
  end

  def persisted?
    false
  end
  
private
  def is_sandbox?
    APP_CONFIG["is_sandbox"]
  end

  
end
