package com.lightdevel.wephuot.socialcircle.models.out;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonOut {
    private String userId;

    private String profilePic;

    private String displayName;

    private String email;
}
