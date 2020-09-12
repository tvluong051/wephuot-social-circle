package com.lightdevel.wephuot.socialcircle.models.entities.relationships;

import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Person;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@RelationshipEntity(type = "TRAVELED_WITH")
public class TripBuddy {
    @Id
    @GeneratedValue
    private Long relationId;

    @StartNode
    private Person person;

    @EndNode
    private Person buddy;

    private int weight;
}
