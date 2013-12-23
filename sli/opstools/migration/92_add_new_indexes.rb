#!/usr/bin/env ruby

# Add new indexes to bellSchedule and classPeriod collection in tenant database

# Run this script with no arguments to see usage:
#
#   ruby 92_add_new_indexes.rb
#
# You can give one or more tenant IDs whose databases will be migrated, or "--all"
# for all tenants.  Also you can optionally give the MongoDB host/port.

require 'mongo'

############################################################
# Migration Script
############################################################

@conn = nil
DEBUG = false
CLASS_PERIOD = "classPeriod"
BELL_SCHEDULE = "bellSchedule"


def updateDB(dbName, collection, index)
    puts "Updating index on " + collection
    
    #Check if there is an existing applicationId index, if so drop it first, then recreate the index
    index_lists = @conn[dbName][collection].index_information
    
    puts "------------------------Existing Indexes----------------------------" if DEBUG == true
    puts index_lists.map{|key, value| "#{key}: #{value}" } if DEBUG == true

    key, value, contains = has_index(index_lists, index)

    if  contains
      puts "There is an existing index " + index + " named " + key
      puts "Dropping index ..." + index
      @conn[dbName][collection].drop_index(key)
      value.delete("name")
      value["unique"] = false
      value["sparse"] = false
      puts "Creating index " + index
      @conn[dbName][collection].create_index(index,value)
    else
      puts "Creating index " + index
      @conn[dbName][collection].create_index(index,:unique => false,:sparse => false)
    end
      puts "Finishing creating index on db " + dbName
end

#Check if there is an existing index, return true if there exists
def has_index(index_lists, index)
  contains = false
  index_lists.each do |key, value|
    if has_index_helper(value, index)
      contains = true
      return key, value, contains
    end
  end
  return nil, nil, contains
end

#A helper method of method has_index() to check if an index has key, return true if there exists
def has_index_helper(index_map, index)
  return  index_map["key"].has_key?(index)
end


# Example: this_script.rb --all host:myhost.com port:12345
#          this_script.rb host:myhost.com port:12345 db1 db2 db3
#
def parseArgs(argv)

  result = { "all" => false, "mongo_host" => "localhost", "mongo_port" => 27017, "tenants" => [] }

  for arg in argv
    if arg == "--all"
      result["all"] = true
    elsif arg.include?(":")
      host_port = arg.split(':')
      result["mongo_host"] = host_port[0]
      result["mongo_port"] = host_port[1]
    else
      result["tenants"] << arg
    end
  end
  return result
end

# Main driver
def main(argv)
  params = parseArgs(argv)

  tenants = params["tenants"]
  @conn = Mongo::Connection.new(params["mongo_host"], params["mongo_port"])

  # Map tenant name to database by getting records in sli.tenant collection
  tenant2db = {}
  @conn['sli']['tenant'].find({}).each do |tenant|
    name = tenant['body']['tenantId']
    db = tenant['body']['dbName']
    tenant2db[name] = db
  end

  # Make sure any tenants given explicitly actually exist
  for tname in tenants
    if not tenant2db.has_key?(tname)
      puts "No such tenant: " + tname
      return
    end
  end

  # Make sure that either a list of tenants, or "-all" was given (and not both)
  if ( 0==tenants.length and not params["all"] ) or ( tenants.length >= 1 and params["all"] )
    puts "--------------------------------------------------------------------------------------------"
    puts "| Tenant databases on server '" + params["mongo_host"] + "':"
    tenant2db.each_pair do |tname, dbname|
      puts "|    " + tname + " " + dbname
    end

    puts "--------------------------------------------------------------------------------------------"
    puts "| To Add new indexes to bellSchedule and classPeriod collection, give argument(s) as follows:\n"
    puts "|\n"
    puts "|     --all                                     Migrate against all tenant dbs\n"
    puts "|       --OR--\n"
    puts "|     <tenant1> [<tenant2> ...]                 Migrate only database(s) for the given tenant(s)\n"
    puts "|\n"
    puts "|    myhost:myport                              Optional hostname and port defaults to localhost:27017"
    puts "--------------------------------------------------------------------------------------------"
    return
  end

  # Set tenants to complete list
  if params["all"]
    tenants = tenant2db.keys
  end

  for tenant in tenants
    dbname = tenant2db[tenant]
    puts "Updating or Creating index of classPeriod and bellSchedule collection for tenant: " + tenant + ", database " + dbname
    updateDB(dbname, CLASS_PERIOD, "body.educationOrganizationId")
    updateDB(dbname, BELL_SCHEDULE, "body.meetingTime.classPeriodId")
    updateDB(dbname, BELL_SCHEDULE, "body.educationOrganizationId")
    puts "\n"
  end

  puts "    " + "All done."
end

# Run it
main(ARGV)