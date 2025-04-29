package com.damian.debtto.common.utils;

import com.damian.debtto.customer.Customer;
import com.damian.debtto.customer.CustomerDTO;
import com.damian.debtto.customer.profile.Profile;
import com.damian.debtto.customer.profile.ProfileDTO;
import com.damian.debtto.customer.profile.exception.ProfileNotFoundException;

import java.util.Optional;

public class DTOMapper {
    public static CustomerDTO build(Customer customer) {
        ProfileDTO profileDTO = Optional.ofNullable(customer.getProfile().toDTO())
                                        .orElseThrow(ProfileNotFoundException::new);

        return new CustomerDTO(
                customer.getId(),
                customer.getEmail(),
                customer.getRole(),
                customer.getAccountStatus(),
                profileDTO,
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    public static ProfileDTO build(Profile profile) {
        return new ProfileDTO(
                profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getPhone(),
                profile.getBirthdate(),
                profile.getGender(),
                profile.getPhotoPath(),
                profile.getAddress(),
                profile.getPostalCode(),
                profile.getCountry(),
                profile.getNationalId(),
                profile.getUpdatedAt()
        );
    }

}
