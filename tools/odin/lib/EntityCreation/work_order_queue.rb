=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

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
require 'singleton'
require_relative '../OutputGeneration/entity_queue'

class WorkOrderQueueRouter < Fiber

  @entityQueue = nil
  @workOrderQueue = nil
  @factory = nil

  def initialize(factory, workOrderQueue, entityQueue)
    @factory = factory
    @workOrderQueue = workOrderQueue
    @entityQueue = entityQueue
  end

  def resume
    while !@workOrderQueue.empty?
      work_order = @workOrderQueue.pop_work_order
      entity = @factory.create(work_order)
      @entityQueue.push_entity(entity)
    end
  end

end

class WorkOrderQueue

  def initialize()
    @work_orders = []
    @router = nil
  end

  def factory(factory, entityQueue)
    @router = WorkOrderQueueRouter.new(factory, self, entityQueue) do yield end
  end

  def push_work_order(work_order)
    @work_orders << work_order
    @router.resume if @router
  end

  def pop_work_order
    @work_orders.pop
  end

  def get_work_orders(work_order_type)
    orders = []
    @work_orders.each do |order|
      orders << order if( is_type(order, work_order_type) )
    end
    orders
  end

  def count(work_order_type)
    count = 0
    @work_orders.each do |order|
      count += 1 if ( is_type(order, work_order_type) )
    end
    count
  end

  def is_type(order, order_type)
    (order.kind_of? order_type) || (order.kind_of?(Hash) && (order[:type].to_s == order_type.to_s))
  end

  def empty?
    @work_orders.empty?
  end

end
