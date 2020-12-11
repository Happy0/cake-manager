package com.waracle.cakemgr.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("cake")
public class CakeEntity {

    /**
     * The ID of the cake
     */
    @Id
    private Integer id;

    /**
     * The short title (name) of the cake
     */
    private String title;

    /**
     * A description of the cake
     */
    @JsonProperty("desc")
    private String description;

    /**
     * A URL of an image of the cake
     */
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}