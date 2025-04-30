package com.test.customer.infrastructure.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZipCodeResponse {
    @JsonProperty("post code")
    private String postCode;
    @JsonProperty("country")
    private String country;
    @JsonProperty("country abbreviation")
    private String countryAbbreviation;
    private List<Place> places;

    @Data
    public static class Place {
        @JsonProperty("place name")
        private String placeName;
        private String state;
        @JsonProperty("state abbreviation")
        private String stateAbbreviation;
    }
}