class Entity < SessionResource
  self.site = APP_CONFIG['api_base']
  
  def self.get_simple_and_complex(parameters)
    base = get(parameters)
    entity = []
    if base.is_a?(Array)
      type = nil
      return entity if base.first.nil?
      type = VIEW_CONFIG[base.first['entityType']] if base.first.has_key? 'entityType'
      base.each do |single|
        one = Hash.new
        one[:simple] = build_simple_hash(type, single)
        one[:complex] = single
        entity.push(one)
      end
    else
      entity.push({:complex => base, :simple => nil})
    end
    entity
  end
  
  def self.build_simple_hash(type, hash)
    return nil if hash.nil?
    type = get_basic_types(hash) if type.nil?
    one = {}
    type.each do |item|
      final_type = item.split('/').last
      one[final_type] = value_for_simple_view(item, hash)
    end
    one
  end
  
  def self.value_for_simple_view (type, hash)
    return nil if hash.nil? or type.nil?
    return hash[type] unless type.include? '/'
    type_split = type.split '/'
    temp_hash = hash
    type_split.each do |split|
      temp_hash = temp_hash[split]
    end
    temp_hash
  end
  
  def self.get_basic_types(hash)
    types = []
    hash.keys.each do |key|
      if !hash[key].is_a?(Hash) and !hash[key].is_a?(Array) and key != 'entityType'
        types.push(key)
      end
    end
    types.slice(0, 5)
  end
end
