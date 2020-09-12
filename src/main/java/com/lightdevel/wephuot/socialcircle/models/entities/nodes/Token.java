package com.lightdevel.wephuot.socialcircle.models.entities.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    private String token;
    private Long expiresTs;
}
