package com.lightdevel.wephuot.socialcircle.repositories;

import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, String> {

    @Query("MATCH (p:Person {id: {personId}}) -[r:TRAVELED_WITH]- (buddy:Person)" +
            "RETURN buddy " +
            "ORDER BY r.weight " +
            "LIMIT {limit}")
    List<Person> findTopFriends(@Param("personId") String personId, @Param("limit") Integer limit);

    List<Person> findByEmailMatchesRegex(String emailRegex);

}
