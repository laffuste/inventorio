package com.laffuste.inventorio.interfaces.rest.v1;


import com.intuit.karate.junit5.Karate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
public class KarateTests {

    @Karate.Test
    Karate testAll() {

        ServerStart.start();

        return Karate.run().relativeTo(getClass());
    }

}
