package com.kok.kokcore.places.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
}
