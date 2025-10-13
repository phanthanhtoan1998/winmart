package com.winmart.common.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Cấu hình matching strategy
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
//        // Cấu hình để tránh map vào BaseEntity.id
//        modelMapper.getConfiguration().setPropertyCondition(context -> {
//            String destinationPropertyName = context.getMapping().getLastDestinationProperty().getName();
//            // Không map vào trường id của BaseEntity
//            return !destinationPropertyName.equals("id");
//        });
        
        // Cấu hình để bỏ qua các trường null
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        
        return modelMapper;
    }
} 