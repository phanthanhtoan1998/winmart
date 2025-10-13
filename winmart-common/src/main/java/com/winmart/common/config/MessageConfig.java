package com.winmart.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {
    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages",
                                   "classpath:i18n/common-messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10); //reload messages every 10 seconds
        return messageSource;

    }
}
