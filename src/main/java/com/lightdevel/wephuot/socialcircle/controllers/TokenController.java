package com.lightdevel.wephuot.socialcircle.controllers;

import com.lightdevel.wephuot.socialcircle.models.out.TokenOut;
import com.lightdevel.wephuot.socialcircle.services.TokenService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Api
@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = Objects.requireNonNull(tokenService);
    }

    @GetMapping
    public TokenOut getPersonFromToken(@RequestParam("token") String token,
                                       @RequestParam("tokenProvider") String tokenProvider) {
        log.info("GET - retrieve details of person from token of provider", tokenProvider);
        return this.tokenService.getTokenFromSocialToken(token, tokenProvider);
    }

    @PostMapping("/validation")
    public TokenOut validateToken(@RequestParam("token") String token) {
        log.info("POST - validate token {}", token);
        return this.tokenService.validate(token);
    }
}
