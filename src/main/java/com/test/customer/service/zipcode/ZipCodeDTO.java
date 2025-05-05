package com.test.customer.service.zipcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZipCodeDTO {
    @JsonProperty("post code")
    private String postCode;
    @JsonProperty("country")
    private String country;
    @JsonProperty("country abbreviation")
    private String countryAbbreviation;
    private List<Place> places;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Place {
        @JsonProperty("place name")
        private String placeName;
        private String state;
        @JsonProperty("state abbreviation")
        private String stateAbbreviation;
    }
}