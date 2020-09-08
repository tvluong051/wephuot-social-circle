package com.lightdevel.wephuot.socialcircle.configs;

import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Person;
import com.lightdevel.wephuot.socialcircle.models.entities.relationships.TripBuddy;
import com.lightdevel.wephuot.socialcircle.repositories.SocialProfileRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EntityScan(basePackageClasses = { Person.class, TripBuddy.class })
@EnableNeo4jRepositories(basePackageClasses = { SocialProfileRepository.class })
public class Neo4jConfig {
}
