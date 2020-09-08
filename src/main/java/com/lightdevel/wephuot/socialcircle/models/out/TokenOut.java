package com.lightdevel.wephuot.socialcircle.models.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenOut {
    private String userId;

    private String accessToken;

    private Long expiresTokenTs;
}