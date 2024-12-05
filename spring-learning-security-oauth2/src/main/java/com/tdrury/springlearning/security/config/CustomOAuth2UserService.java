package com.tdrury.springlearning.security.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("loadUser: userRequest.token.type={}", userRequest.getAccessToken().getTokenType().getValue());
        log.info("loadUser: userRequest.token.scopes={}", userRequest.getAccessToken().getScopes());
        log.info("loadUser: userRequest.token.expires={}", userRequest.getAccessToken().getExpiresAt());
        log.info("loadUser: userRequest.registration={}", userRequest.getClientRegistration());
        log.info("loadUser: userRequest.additionalParms={}", userRequest.getAdditionalParameters());
        OAuth2User user = super.loadUser(userRequest);
        log.info("loadUser: user={}", user);
        return user;
    }

}