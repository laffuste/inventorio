package com.laffuste.inventorio.interfaces.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "inventorio")
@Data
public class InventorioConfig {

    private Integer productPaging;

}
