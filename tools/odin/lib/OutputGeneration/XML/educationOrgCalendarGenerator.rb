=begin

Copyright 2012 Shared Learning Collaborative, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end

require "mustache"

require_relative "interchangeGenerator.rb"
require_relative "../../Shared/data_utility.rb"

Dir["#{File.dirname(__FILE__)}/../EntityClasses/*.rb"].each { |f| load(f) }

# event-based education organization interchange generator
class EducationOrgCalendarGenerator < InterchangeGenerator
  
  # session writer
  class SessionWriter < Mustache
    def initialize(batch_size)
      @template_file = "#{File.dirname(__FILE__)}/interchangeTemplates/Partials/session.mustache"
      @batch_size    = batch_size
      @entities      = []
    end
    def sessions
      @entities
    end
    def write(handle, entity)
      @entities << entity
      if @entities.size >= @batch_size
        handle.write render
        @entities = []
      end
    end
    def flush(handle)
      handle.write render
    end
  end

  # grading period writer
  class GradingPeriodWriter < Mustache
    def initialize(batch_size)
      @template_file = "#{File.dirname(__FILE__)}/interchangeTemplates/Partials/grading_period.mustache"
      @batch_size    = batch_size
      @entities      = []
    end
    def grading_periods
      @entities
    end
    def write(handle, entity)
      @entities << entity
      if @entities.size >= @batch_size
        handle.write render
        @entities = []
      end
    end
    def flush(handle)
      handle.write render
    end
  end

  # calendar date writer
  class CalendarDateWriter < Mustache
    def initialize(batch_size)
      @template_file = "#{File.dirname(__FILE__)}/interchangeTemplates/Partials/calendar_date.mustache"
      @batch_size    = batch_size
      @entities      = []
    end
    def calendar_dates
      @entities
    end
    def write(handle, entity)
      @entities << entity
      if @entities.size >= @batch_size
        handle.write render
        @entities = []
      end
    end
    def flush(handle)
      handle.write render
    end
  end

  # initialization will define the header and footer for the education organization calendar interchange
  # writes header to education organization calendar interchange
  # leaves file handle open for event-based writing of ed-fi entities
  def initialize(batch_size)
    @header, @footer = build_header_footer( "EducationOrgCalendar" )
    @handle = File.new("generated/InterchangeEducationOrgCalendar.xml", 'w')
    @handle.write(@header)

    @session_writer        = SessionWriter.new(batch_size)
    @grading_period_writer = GradingPeriodWriter.new(batch_size)
    @calendar_date_writer  = CalendarDateWriter.new(batch_size)
  end

  # creates and writes session to interchange
  def create_session(name, year, term, interval, ed_org_id, grading_periods)
  	@session_writer.write(@handle, Session.new(name, year, term, interval, ed_org_id, grading_periods))
  end

  # creates and writes grading period to interchange
  def create_grading_period(type, year, interval, ed_org_id, calendar_dates)
    @grading_period_writer.write(@handle, GradingPeriod.new(type, year, interval, ed_org_id, calendar_dates))
  end

  # creates and writes calendar date to interchange
  def create_calendar_date(date, event)
    @calendar_date_writer.write(@handle, CalendarDate.new(date, event))
  end

  # writes footer and closes education organization calendar interchange file handle
  def close
    @session_writer.flush(@handle)
    @grading_period_writer.flush(@handle)
    @calendar_date_writer.flush(@handle)
    @handle.write(@footer)
    @handle.close()
  end
end