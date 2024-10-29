package com.project.BooksAndAuthors.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig
{
    @Bean
    public ModelMapper modelMapper()
    {
        //setting the model mapper to hande cascade correctly
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        //skips every null field(keeps the previous one)
        //modelMapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
        return modelMapper;
    }

    //i hope it works??
    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }
}
