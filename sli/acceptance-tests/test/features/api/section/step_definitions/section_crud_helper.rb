# lazy initialization of parsed_results so we don't reparse for every "Then.."
def parsed_results
  @parsed_results ||= JSON.parse(@res.body)
end

# Function data_builder
# Inputs: None
# Output: Data object in json or XML format depending on what the @format variable is set to
# Returns: Nothing, see Output
# Description: Helper function to create json or XML data structures to PUT or POST 
#                   to reduce replication of code
def data_builder
  if @format == "application/json"
  data = Hash[
    "sectionCode" => @section_code,
    "courseSequence" => @course_seq,
    "educationEnvironment" => @edu_env,
    "instructionMedium" => @medium,
    "populationServed" => @pop,
    "availableCredit" => @credit
  ]
  data = data.to_json
  elsif @format == "application/xml"
    builder = Builder::XmlMarkup.new(:indent => 2)
    data = builder.section { |b| 
      b.sectionCode(@section_code)
      b.courseSequence(@course_seq)
      b.educationalEnvironment(@edu_env)
      b.populationServed(@pop)
      b.availableCredit(@credit)  
      }
  else
    assert(false, "Unsupported MIME type: #{@format}")
  end
  data
end