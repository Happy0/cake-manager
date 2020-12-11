package com.waracle.cakemgr.controllers.validators;

import com.waracle.cakemgr.entities.CakeEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Validates that a cake is a valid cake for storage in the system.
 */
public class CakeValidator {

    private static final int CAKE_DESCRIPTION_MAX_LENGTH = 500;
    private static final int CAKE_TITLE_LENGTH = 100;
    private static final int CAKE_TITLE_IMAGE_URL_LENGTH = 1000;

    /**
     * Validates a cake, returning an error message if the cake is invalid or Empty otherwise.
     *
     * @param cakeEntity the cake to validate
     * @return An error string if the cake is invalid, otherwise returns 'empty'
     */
    public static Optional<String> validateCake(CakeEntity cakeEntity) {
        return testFieldsPopulated(cakeEntity)
                .or(() -> testLengths(cakeEntity));
    }

    /**
     * Tests none of the required fields are null or empty
     * @param cakeEntity the cake entity
     * @return an error if a required field is missing
     */
    private static Optional<String> testFieldsPopulated(CakeEntity cakeEntity) {
        if (StringUtils.isBlank(cakeEntity.getImage())) {
            return Optional.of("Cake image URL cannot be empty.");
        } else if (StringUtils.isBlank(cakeEntity.getTitle())) {
            return Optional.of("Cake image title cannot be empty.");
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Tests if any of the fields don't meet the required length
     *
     * @param cakeEntity the cake entity
     * @return an error if a field doesn't meet the length requirements
     */
    private static Optional<String> testLengths(CakeEntity cakeEntity) {
        if (cakeEntity.getDescription().length() > CAKE_DESCRIPTION_MAX_LENGTH) {
            return Optional.of("Cake description length must be less than or equal to" + CAKE_DESCRIPTION_MAX_LENGTH);
        } else if (cakeEntity.getTitle().length() > CAKE_TITLE_LENGTH) {
            return Optional.of("Cake title length must be less than or equal to" + CAKE_TITLE_LENGTH);
        } else if (cakeEntity.getImage().length() > CAKE_TITLE_IMAGE_URL_LENGTH) {
            return Optional.of("Cake image URL must be less than or equal to " + CAKE_TITLE_IMAGE_URL_LENGTH);
        } else {
            return Optional.empty();
        }
    }

}
