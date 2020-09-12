package com.lightdevel.wephuot.socialcircle.models.in;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SocialProfileIn {
    @NotNull
    private String provider;

    @NotNull
    private String providedId;

    private List<String> emails;

    private String profilePicture;

    private String displayName;
}
