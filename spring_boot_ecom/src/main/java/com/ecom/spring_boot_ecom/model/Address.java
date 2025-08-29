package com.ecom.spring_boot_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street name must be at least 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5, message = "building name must be at least 2 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "city name must be at least 4 characters long")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters long")
    private String state;

    @NotBlank
    @Size(min = 2, message = "country name must be at least 2 characters long")
    private String country;

    @NotBlank
    @Size(min = 6, message = "pinCode must be at least 6 characters long")
    @Column(name = "pincode")
    private String pinCode;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String pinCode, String country, String state, String city, String buildingName, String street) {
        this.pinCode = pinCode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.buildingName = buildingName;
        this.street = street;
    }
}
