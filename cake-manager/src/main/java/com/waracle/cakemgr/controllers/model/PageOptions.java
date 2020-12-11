package com.waracle.cakemgr.controllers.model;

import java.util.Optional;

/**
 * Class for validating page ranges and returning the page range as an SQL query offset.
 */
public class PageOptions {

    private final int limit;
    private final int page;

    /**
     * Private constructor. This class may only be constructed via validating
     * factory method
     *
     * @param page the page (indexes start from 1). Must be >= 1
     * @param limit the limit (must be >=1)
     */
    private PageOptions(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    /**
     * @return The offset of the first element in the page when the collection is a contiguous
     * collection starting from 1
     */
    public int getAsOffset() {
        return (this.page -1) * getLimit();
    }

    /**
     * @return the number of results to return for this page
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Validates paging parameters, and returns 'empty' if invalid range.
     *
     * Page indexes begin at 1.
     *
     * @param page the page (indexes start from 1.)
     * @param size the page size
     * @return a newly constructed PageOptions object if valid, empty otherwise
     */
    public static Optional<PageOptions> validated(Integer page, Integer size) {
        Optional<Integer> optionalPage = Optional.ofNullable(page);

        if (page == null && size == null) {
            // Cannot give null arguments for both as we don't want to define a default limit here
            // as that is a decision for the caller
            return Optional.empty();
        }
        else if (page != null && size == null) {
            // Cannot define a page, but no page size
            return Optional.empty();
        } else if (invalidPageRange(page, size)) {
            // Cannot supply arguments with values less than 1
            return Optional.empty();
        }
         else {
            int pageNumber = optionalPage.orElse(1);

            return Optional.of(new PageOptions(pageNumber, size));
        }

    }

    private static boolean invalidPageRange(Integer page, Integer size) {
        return ((page != null && page <= 0) || (size != null && size <= 0));
    }



}
