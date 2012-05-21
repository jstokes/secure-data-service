require 'mongo'
require 'json'

connection = Mongo::Connection.new("nxmongo.slidev.org", 27017)
db = connection.db("sli")
coll = db.collection("system.profile")

cursor = coll.find()

arr=[]
totals={}
cursor.each do |doc|
  arr<<doc["ns"] unless arr.include?(doc["ns"])
  
  totals[doc["ns"]].nil? ? totals[doc["ns"]]=doc["millis"] : totals[doc["ns"]]+=doc["millis"]   
      
end

puts arr.inspect
puts totals.inspect