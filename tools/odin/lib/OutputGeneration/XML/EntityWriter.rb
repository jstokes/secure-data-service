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
require 'mustache'
require_relative './TemplateCache'

class EntityWriter < Mustache

  def initialize(template_name)
    @template_cache = TemplateCache.instance()
    @template_name = template_name
    @entity = nil

    ## Enable this to debug mustache issues. This asserts for any missing context lookups.
    # @raise_on_context_miss = true
  end

  def partial(name)
    @template_cache.templates["#{name}"]
  end

  def entity
    @entity
  end

  def write(entity)
    @entity = entity
    render(@template_cache.templates[@template_name], entity)
  end

end
