package com.lightdevel.wephuot.socialcircle.services;

import com.lightdevel.wephuot.socialcircle.clients.Provider;
import com.lightdevel.wephuot.socialcircle.clients.ProviderFactory;
import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Person;
import com.lightdevel.wephuot.socialcircle.models.entities.nodes.Token;
import com.lightdevel.wephuot.socialcircle.models.out.TokenOut;
import com.lightdevel.wephuot.socialcircle.repositories.PersonRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    private static final Long TOKEN_TTL = 12 * 3600 *1000L; // 12 hours
    private static final String SECRET_TOKEN_KEY_BASE = "Spn2na2Ua1sc!lmpaA";
    private static final String SECRET_TOKEN_KEY = new StringBuilder(SECRET_TOKEN_KEY_BASE).reverse()
            .append(SECRET_TOKEN_KEY_BASE).toString();

    private final PersonRepository personRepository;
    private final ProviderFactory providerFactory;

    @Autowired
    public TokenServiceImpl(PersonRepository personRepository,
                             ProviderFactory providerFactory) {
        this.personRepository = Objects.requireNonNull(personRepository);
        this.providerFactory = Objects.requireNonNull(providerFactory);
    }

    @Override
    public TokenOut getTokenFromSocialToken(String token, String tokenProvider) {
        Provider provider = providerFactory.getProvider(tokenProvider);
        if (provider == null) {
            return null;
        }
        String providedId = provider.getProvidedIdFromToken(token);
        if(providedId == null) {
            return null;
        }
        Person person = this.personRepository.findByProfilesProviderAndProfilesProvidedId(tokenProvider, providedId);

        if (person.getAccessToken() == null || Calendar.getInstance().getTimeInMillis() > person.getAccessToken().getExpiresTs()) {
            Token internalToken = generateToken(person);
            person.setAccessToken(internalToken);
            person = personRepository.save(person);
        }
        Token internalToken = person.getAccessToken();
        return TokenOut.builder()
                .userId(person.getId())
                .accessToken(internalToken.getToken())
                .expiresTokenTs(internalToken.getExpiresTs())
                .build();
    }

    @Override
    public TokenOut validate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_TOKEN_KEY.getBytes()))
                    .parseClaimsJws(token)
                    .getBody();
            return TokenOut.builder()
                    .userId(claims.getSubject())
                    .expiresTokenTs(claims.getExpiration().getTime())
                    .build();
        } catch (Exception exception) {
            log.error("Error: ", exception);
        }
        return null;
    }

    private Token generateToken(Person person) {
        long expiresTs = DateTime.now().getMillis() + TOKEN_TTL;
        String tokenValue = Jwts.builder()
                .setSubject(person.getId())
                .setExpiration(Date.from(Instant.ofEpochMilli(expiresTs)))
                .signWith(Keys.hmacShaKeyFor(SECRET_TOKEN_KEY.getBytes()))
                .compact();

        Token token = person.getAccessToken() == null ? new Token() : person.getAccessToken();
        token.setExpiresTs(expiresTs);
        token.setToken(tokenValue);
        return token;
    }

}
