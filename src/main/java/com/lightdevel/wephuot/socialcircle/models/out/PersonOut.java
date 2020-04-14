package com.lightdevel.wephuot.socialcircle.models.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonOut {
    private String userId;

    private String profilePic;

    private String displayName;

    private String email;
}
