package com.lightdevel.wephuot.socialcircle.models.entities.nodes;

import com.lightdevel.wephuot.socialcircle.models.entities.relationships.TripBuddy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@Data
@NodeEntity
public class Person {
    @Id
    private String id;

    private String email;

    private String profilePicture;

    private String displayName;

    @Relationship(type = "HAS_EXTERNAL_PROFILE")
    private List<SocialProfile> profiles;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "TRAVELED_WITH", direction = Relationship.UNDIRECTED)
    private List<TripBuddy> buddies;

    @Relationship(type = "HAS_TOKEN")
    private Token accessToken;


}
