package com.lightdevel.wephuot.socialcircle.services;

import com.lightdevel.wephuot.socialcircle.models.in.SocialProfileIn;
import com.lightdevel.wephuot.socialcircle.models.out.PersonOut;

import java.util.List;

public interface PersonService {
    List<PersonOut> findPerson(String provider, String providedId);

    List<PersonOut> getTopFriends(String personId, Integer topSize);

    PersonOut saveProfile(SocialProfileIn profile);

    PersonOut getPersonDetail(String personId);

    List<PersonOut> searchPerson(String searchTerm);
}
