package com.lightdevel.wephuot.socialcircle.services;


import com.lightdevel.wephuot.socialcircle.exceptions.BusinessException;
import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Person;
import com.lightdevel.wephuot.socialcircle.models.entities.nodes.SocialProfile;
import com.lightdevel.wephuot.socialcircle.models.in.SocialProfileIn;
import com.lightdevel.wephuot.socialcircle.models.out.PersonOut;
import com.lightdevel.wephuot.socialcircle.repositories.PersonRepository;
import com.lightdevel.wephuot.socialcircle.repositories.SocialProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;
    private SocialProfileRepository profileRepository;

    @Autowired
    public PersonServiceImpl(SocialProfileRepository profileRepository, PersonRepository personRepository) {
        this.profileRepository = Objects.requireNonNull(profileRepository);
        this.personRepository = Objects.requireNonNull(personRepository);
    }

    @Override
    public List<PersonOut> findPerson(String provider, String providedId) {
        return this.profileRepository.findByProviderAndProvidedId(provider, providedId)
            .stream()
            .map(profile -> PersonOut.builder()
                .userId(profile.getIdentity().getId())
                .profilePic(
                    StringUtils.isEmpty(profile.getIdentity().getProfilePicture()) ?
                        profile.getProfilePicture() : profile.getIdentity().getProfilePicture()
                )
                .displayName(
                    StringUtils.isEmpty(profile.getIdentity().getDisplayName()) ?
                        profile.getDisplayName() : profile.getIdentity().getDisplayName()
                )
                .email(profile.getIdentity().getEmail())
                .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public List<PersonOut> getTopFriends(String personId, Integer topSize) {
        return this.personRepository.findTopFriends(personId, topSize)
            .stream()
            .map(person -> PersonOut.builder()
                .userId(person.getId())
                .displayName(person.getDisplayName())
                .profilePic(person.getProfilePicture())
                .email(person.getEmail())
                .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public PersonOut saveProfile(SocialProfileIn profile) {
        SocialProfile updatedProfile;
        Person updatedPerson;
        List<SocialProfile> existingProfiles =this.profileRepository.findByProviderAndProvidedId(profile.getProvider(), profile.getProvidedId());
        if(CollectionUtils.isEmpty(existingProfiles)) {
            updatedPerson = new Person();
            updatedPerson.setId(UUID.randomUUID().toString());
            updatedProfile = new SocialProfile();
        } else if(existingProfiles.size() == 1) {
            updatedProfile = existingProfiles.get(0);
            updatedPerson = updatedProfile.getIdentity();
        } else {
            throw new BusinessException();
        }
        updatedPerson.setDisplayName(profile.getDisplayName());
        updatedPerson.setProfilePicture(profile.getProfilePicture());
        if(!CollectionUtils.isEmpty(profile.getEmails())) {
            updatedPerson.setEmail(profile.getEmails().get(0));
        }
        updatedPerson = this.personRepository.save(updatedPerson);

        updatedProfile.setProvider(profile.getProvider());
        updatedProfile.setProvidedId(profile.getProvidedId());
        updatedProfile.setDisplayName(profile.getDisplayName());
        updatedProfile.setProfilePicture(profile.getProfilePicture());
        updatedProfile.setDisplayName(profile.getDisplayName());
        updatedProfile.setEmails(profile.getEmails());
        updatedProfile.setIdentity(updatedPerson);
        updatedProfile = this.profileRepository.save(updatedProfile);

        return PersonOut.builder()
            .userId(updatedProfile.getIdentity().getId())
            .profilePic(updatedProfile.getProfilePicture())
            .displayName(updatedProfile.getDisplayName())
            .email(updatedProfile.getIdentity().getEmail())
            .build();
    }

    @Override
    public PersonOut getPersonDetail(String personId) {
        Optional<Person> optionalPerson = this.personRepository.findById(personId);
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            return PersonOut.builder()
                    .userId(person.getId())
                    .profilePic(person.getProfilePicture())
                    .displayName(person.getDisplayName())
                    .email(person.getEmail())
                    .build();
        }
        return PersonOut.builder().userId(personId).build();
    }

    @Override
    public List<PersonOut> searchPerson(String searchTerm) {
        String searchTermRegex = "(?i).*" + searchTerm + ".*";
        List<SocialProfile> profilesByName = this.profileRepository.findByDisplayNameMatchesRegex(searchTermRegex);
        Set<Person> persons = profilesByName.stream().map(SocialProfile::getIdentity).collect(Collectors.toSet());
        if(persons.size() < 10) {
            persons.addAll(this.personRepository.findByEmailMatchesRegex(searchTermRegex));
        }
        return persons.stream()
                .map(person -> PersonOut.builder()
                        .userId(person.getId())
                        .displayName(person.getDisplayName())
                        .profilePic(person.getProfilePicture())
                        .email(person.getEmail())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
