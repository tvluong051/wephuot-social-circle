package com.lightdevel.wephuot.socialcircle.services;

import com.lightdevel.wephuot.socialcircle.models.out.TokenOut;

public interface TokenService {
    TokenOut getTokenFromSocialToken(String token, String tokenProvider);

    TokenOut validate(String token);
}
