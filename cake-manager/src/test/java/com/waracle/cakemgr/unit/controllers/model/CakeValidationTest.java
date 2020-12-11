package com.waracle.cakemgr.unit.controllers.model;

import com.waracle.cakemgr.controllers.validators.CakeValidator;
import com.waracle.cakemgr.entities.CakeEntity;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Cake validation unit tests
 */
public class CakeValidationTest {

    // TODO: Missing tests: empty fields

    /**
     * Tests a cake with valid attributes
     */
    @Test
    public void testValidCake() {
        CakeEntity cakeEntity = new CakeEntity();

        cakeEntity.setTitle("Chocolate cake.");
        cakeEntity.setDescription("Delicious cake");
        cakeEntity.setImage("url");

        Optional<String> error = CakeValidator.validateCake(cakeEntity);
        Assert.assertTrue(error.isEmpty());
    }

    /**
     * Tests that an error is returned for cakes with too long a title
     */
    @Test
    public void testInvalidTitleLength() {
        CakeEntity cakeEntity = new CakeEntity();

        String title = StringUtils.repeat("a", 101);
        cakeEntity.setTitle(title);
        cakeEntity.setDescription("An invalid cake");
        cakeEntity.setImage("an invalid url");

        Optional<String> error = CakeValidator.validateCake(cakeEntity);

        Assert.assertTrue(error.isPresent());
    }

    /**
     * Tests that an error is returned for cakes with too long a description
     */
    @Test
    public void testInvalidDescriptionLength() {
        CakeEntity cakeEntity = new CakeEntity();

        String description = StringUtils.repeat("a", 501);
        cakeEntity.setTitle("Chocolate cake");
        cakeEntity.setDescription(description);
        cakeEntity.setImage("an invalid url");

        Optional<String> error = CakeValidator.validateCake(cakeEntity);

        Assert.assertTrue(error.isPresent());
    }

    /**
     * Tests that an error is returned for cakes with too long an image URL
     */
    @Test
    public void testInvalidImageLength() {
        CakeEntity cakeEntity = new CakeEntity();

        String url = StringUtils.repeat("a", 1001);
        cakeEntity.setTitle("Chocolate cake");
        cakeEntity.setDescription("Delicious cake");
        cakeEntity.setImage(url);

        Optional<String> error = CakeValidator.validateCake(cakeEntity);

        Assert.assertTrue(error.isPresent());
    }


}
