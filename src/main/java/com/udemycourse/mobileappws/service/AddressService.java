package com.udemycourse.mobileappws.service;

import com.udemycourse.mobileappws.shared.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {

    List<AddressDTO> getAddresses(String userId);

    AddressDTO getAddress(String addressId);
}
