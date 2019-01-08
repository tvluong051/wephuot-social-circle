package com.lightdevel.wephuot.socialcircle.models.entities.nodes;

import lombok.*;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@Data
@NodeEntity
public class SocialProfile {

    @Id
    @GeneratedValue
    private Long profileId;

    private String provider;

    private String providedId;

    private List<String> emails;

    private String profilePicture;

    private String displayName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "HAS_EXTERNAL_PROFILE", direction = Relationship.INCOMING)
    private Person identity;
}
