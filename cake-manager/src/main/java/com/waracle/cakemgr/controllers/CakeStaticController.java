package com.waracle.cakemgr.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Serves the static web content
 */
@Controller
public class CakeStaticController {

    /**
     * Serve the HTML, js and css files in the 'static' resources folder.
     * @return
     */
    @Bean
    RouterFunction<ServerResponse> staticResourceRouter(){
        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
    }

}
