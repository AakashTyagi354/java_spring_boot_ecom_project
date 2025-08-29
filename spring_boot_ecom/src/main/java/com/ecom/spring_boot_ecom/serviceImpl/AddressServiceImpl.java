package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.model.Address;
import com.ecom.spring_boot_ecom.model.User;
import com.ecom.spring_boot_ecom.payload.AddressDTO;
import com.ecom.spring_boot_ecom.repo.AddressRepo;
import com.ecom.spring_boot_ecom.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AddressRepo addressRepo;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO,Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepo.save(address);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        if(!addresses.isEmpty()){
            return addresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        }else{
            throw new RuntimeException("No addresses found for the user");
        }

    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address savedAddress = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));
        savedAddress.setCity(addressDTO.getCity());
        savedAddress.setState(addressDTO.getState());
        savedAddress.setCountry(addressDTO.getCountry());
        savedAddress.setPinCode(addressDTO.getPinCode());
        savedAddress.setBuildingName(addressDTO.getBuildingName());
        savedAddress.setStreet(addressDTO.getStreet());
        addressRepo.save(savedAddress);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address savedAddress = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));
        addressRepo.delete(savedAddress);
        return "Address deleted successfully";
    }
}
