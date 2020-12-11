package com.waracle.cakemgr.repositories;

import com.waracle.cakemgr.entities.CakeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for cakes
 */
public interface CakeRepository extends ReactiveSortingRepository<CakeEntity, String> {

    /**
     * A pageable version of findAll.
     *
     * @param offset the offset to begin at in the collection of cakes stored in the database
     * @param limit the number of cakes to return
     * @return
     */
    // Note: the flux database driver doesn't appear to support spring data's
    // 'Pageable' parameter yet so we require a custom query for paging options.
    @Query("SELECT * FROM cake LIMIT :limit OFFSET :offset")
    Flux<CakeEntity> findAll(int offset, int limit);

}
