require 'net/http'
require 'uri'
require 'rexml/document'
require 'json'
require 'jenkins_api_client'

@client = JenkinsApi::Client.new(:server_url => 'http://jenkins.slidev.org', :server_port => '8080',
      :username => '', :password => '', :jenkins_path => '/jenkins', :debug => false)

def get_jobs(view)
  return @client.view.list_jobs(view)
end

def fix_job(job_name, new_branch)
  puts "Adjusting " + job_name + " to " + new_branch  

  job_xml = @client.job.get_config(job_name)

  doc = REXML::Document.new(job_xml)

  doc.elements.each("*/scm/branches/hudson.plugins.git.BranchSpec/name") do |element| 
    puts "Current Branch: #{element.text} changing it to #{new_branch}"
    element.text = new_branch
  end

  @client.job.post_config(job_name, doc.to_s)
end

view_to_configure = ARGV[0]
new_branch = ARGV[1]

actual_jobs = get_jobs(view_to_configure)
puts actual_jobs
actual_jobs.each {|j| fix_job(j, new_branch) }