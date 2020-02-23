package com.udemycourse.mobileappws.service.impl;

import com.udemycourse.mobileappws.io.entity.AddressEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import com.udemycourse.mobileappws.io.repository.AddressRepository;
import com.udemycourse.mobileappws.io.repository.UserRepository;
import com.udemycourse.mobileappws.service.AddressService;
import com.udemycourse.mobileappws.shared.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAddresses(String id) {
        List<AddressDTO> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) return returnValue;

        Iterable <AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        for (AddressEntity addressEntity : addresses) {
            returnValue.add(new ModelMapper().map(addressEntity, AddressDTO.class));
        }

        return returnValue;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        AddressDTO returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null) {
            returnValue = new ModelMapper().map(addressEntity, AddressDTO.class);
        }

        return returnValue;
    }
}
