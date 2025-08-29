package com.ecom.spring_boot_ecom.service;

import com.ecom.spring_boot_ecom.model.User;
import com.ecom.spring_boot_ecom.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses(User user);

    AddressDTO getAddressById(Long addressId);

    AddressDTO updateAddress(Long addressId, @Valid AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
