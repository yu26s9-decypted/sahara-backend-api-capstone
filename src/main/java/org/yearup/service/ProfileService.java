package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Profile getByUserId(int userId) {
        Optional<Profile> profile = profileRepository.findById(userId);
        return profile.orElse(null);
    }

    public Optional<Profile> update(int userId, Profile updatedState){
         return profileRepository.findById(userId).map(
                 profile -> {
                     profile.setFirstName(updatedState.getFirstName());
                     profile.setLastName(updatedState.getLastName());
                     profile.setAddress(updatedState.getAddress());
                     profile.setCity(updatedState.getCity());
                     profile.setEmail(updatedState.getEmail());
                     profile.setState(updatedState.getState());
                     profile.setZip(updatedState.getZip());

                     return profileRepository.save(profile);
                 }
         );
    }

    public Profile updateOasisMembership(int userId, Profile updateState){
        return profileRepository.findById(userId).map(
                        user -> {
                            user.setOasis(updateState.isOasis());
                            return profileRepository.save(user);
                        })
                .orElseThrow(() -> new RuntimeException("user does not exist" + userId));

    }
}
