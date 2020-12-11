package com.waracle.cakemgr.controllers;

import com.waracle.cakemgr.controllers.model.PageOptions;
import com.waracle.cakemgr.controllers.validators.CakeValidator;
import com.waracle.cakemgr.entities.CakeEntity;
import com.waracle.cakemgr.repositories.CakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/cakes")
public class CakeRestController {

    @Autowired
    private CakeRepository cakeRepository;

    @GetMapping
    public Flux<CakeEntity> getCakes(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        if (page == null && size == null) {
            // The client wants all cakes in the system
            Flux<CakeEntity> result = cakeRepository.findAll();
            return result;
        }

        // If the client supplies no page, but does supply a size, we default to the first page
        int defaultedPage = Optional.ofNullable(page).orElse(1);

        Optional<PageOptions> pageOptions = PageOptions.validated(defaultedPage, size);

        if (pageOptions.isEmpty()) {
            String error = "Invalid page query parameters. Page cannot be less than 1, and limit must not be null if page is defined.";
            Mono<CakeEntity> errorResult = Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error));
            return errorResult.flux();
        } else {
            PageOptions options = pageOptions.get();
            Flux<CakeEntity> response = cakeRepository.findAll(options.getAsOffset(), options.getLimit());
            return response;
        }
    }

    @PostMapping
    public Mono<CakeEntity> addCake(@RequestBody CakeEntity cake) {
        Optional<String> cakeInvalid = CakeValidator.validateCake(cake);

        if (cakeInvalid.isPresent()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, cakeInvalid.get()));
        } else {
            return cakeRepository.save(cake);
        }
    }


}
