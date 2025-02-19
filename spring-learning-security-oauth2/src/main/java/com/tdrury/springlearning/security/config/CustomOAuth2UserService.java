package com.tdrury.springlearning.security.config;


import com.tdrury.springlearning.security.model.UserInfo;
import com.tdrury.springlearning.security.model.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    final UserInfoRepository userInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("loadUser: userRequest.token.type={}", userRequest.getAccessToken().getTokenType().getValue());
        log.info("loadUser: userRequest.token.scopes={}", userRequest.getAccessToken().getScopes());
        log.info("loadUser: userRequest.token.expires={}", userRequest.getAccessToken().getExpiresAt());
        log.info("loadUser: userRequest.registration={}", userRequest.getClientRegistration());
        log.info("loadUser: userRequest.additionalParms={}", userRequest.getAdditionalParameters());
        OAuth2User user = super.loadUser(userRequest);
        log.info("loadUser: user={}", user);
        Map<String,Object> attributes = user.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        UserInfo userInfo = new UserInfo();
        userInfo.setOauthSource(registrationId);

        // tbd: refactor - move to provider-specific builder.
        // UserInfo id should be a combination of the registrationId and
        // a non-null attribute.
        // github email is optional so use 'login'; e.g. github/tdrury
        // for google email seems to be required. e.g. google/tdrury@gmail.com
        if (registrationId.equals("google")) {
            String email = attributes.get("email").toString();
            String firstName = attributes.get("given_name").toString();
            String lastName = attributes.get("family_name").toString();
            userInfo.setEmail(email);
            userInfo.setFirstName(firstName);
            userInfo.setLastName(lastName);

        } else if (registrationId.equals("github")) {
            String login = attributes.get("login").toString();
            Object emailAttribute = attributes.get("email");
            String email = emailAttribute != null ? emailAttribute.toString() : login+"@private.github.com";
            userInfo.setEmail(email);
            String[] names = attributes.get("name").toString().split(" ");
            if (names.length == 2) {
                userInfo.setFirstName(names[0]);
                userInfo.setLastName(names[1]);
            } else if (names.length == 1) {
                userInfo.setFirstName(names[0]);
            }
        }

        userInfoRepository.save(userInfo);
        log.info("loadUser: saved UserInfo={}", userInfo);
        return user;
    }

}