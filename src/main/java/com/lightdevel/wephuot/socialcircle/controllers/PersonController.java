package com.lightdevel.wephuot.socialcircle.controllers;

import com.lightdevel.wephuot.socialcircle.models.in.SocialProfileIn;
import com.lightdevel.wephuot.socialcircle.models.out.PersonOut;
import com.lightdevel.wephuot.socialcircle.models.out.SearchResult;
import com.lightdevel.wephuot.socialcircle.services.PersonService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = Objects.requireNonNull(personService);
    }

    @GetMapping("")
    public List<PersonOut> findPersonWithExternalProfile(@RequestParam("provider") String provider,
                                                    @RequestParam("providedId") String providedId) {
        LOGGER.info("GET - find user given provider = {} and providedId = {}", provider, providedId);
        return personService.findPerson(provider, providedId);
    }

    @GetMapping("/search")
    public SearchResult<List<PersonOut>> searchPerson(@RequestParam("term") String searchTerm) {
        LOGGER.info("GET - search term = {}", searchTerm);
        List<PersonOut> results = personService.searchPerson(searchTerm);
        return new SearchResult<>(results.size(), results);
    }

    @GetMapping("/person")
    public PersonOut getPersonFromToken(@RequestParam("token") String token,
                                        @RequestParam("tokenProvider") String tokenProvider) {
        LOGGER.info("GET - retrieve details of person from token of provider", tokenProvider);
        return this.personService.getPersonFromToken(token, tokenProvider);
    }

    @GetMapping("/person/{personId}")
    public PersonOut getPersonDetail(@PathVariable("personId") String personId) {
        LOGGER.info("GET - retrieve details of person id = {}", personId);
        return this.personService.getPersonDetail(personId);
    }

    @GetMapping("/person/{personId}/friends")
    public List<PersonOut> getTopFriends(@PathVariable("personId") String personId,
        @RequestParam(value = "limit", required = false) Integer limit) {
        int topListSize = limit == null || limit <= 0 ? 10 : limit;
        LOGGER.info("GET - get top {} friends of user id = {}", topListSize, personId);
        return this.personService.getTopFriends(personId, topListSize);
    }

    @PostMapping("/person")
    public PersonOut saveExternalProfile(@RequestBody SocialProfileIn profile) {
        LOGGER.info("POST - save external profile from provider = {} and providedId = {}", profile.getProvider(), profile.getProvidedId());
        return this.personService.saveProfile(profile);
    }

}
