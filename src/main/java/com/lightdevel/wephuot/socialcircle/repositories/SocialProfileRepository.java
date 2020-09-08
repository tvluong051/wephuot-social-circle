package com.lightdevel.wephuot.socialcircle.repositories;

import com.lightdevel.wephuot.socialcircle.models.entities.nodes.SocialProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface SocialProfileRepository extends Neo4jRepository<SocialProfile, String> {

    List<SocialProfile> findByProviderAndProvidedId(String provider, String providedId);

    List<SocialProfile> findByDisplayNameMatchesRegex(String nameRegex);

}
