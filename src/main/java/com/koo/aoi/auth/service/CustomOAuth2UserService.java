package com.koo.aoi.auth.service;


import com.koo.aoi.auth.domain.OAuthAttributes;
import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.user.repository.AoiUserRepository;
import com.koo.aoi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * OAuth2 로그인 성공 후 사용자 정보를 처리하는 서비스입니다.
 * 소셜 로그인 공급자로부터 받은 사용자 정보를 기반으로, 데이터베이스에 사용자를 저장하거나 업데이트합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    /**
     * OAuth2 공급자로부터 사용자 정보를 가져와 처리한다.
     * @param userRequest OAuth2 사용자 요청 정보
     * @return 인증된 사용자 정보를 담은 OAuth2User 객체
     * @throws OAuth2AuthenticationException 인증 과정에서 발생하는 예외
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        AoiUser user = userService.register(attributes);

        // 핸들러에서 사용할 수 있도록 정제된 사용자 정보를 담은 새로운 Map을 생성합니다.
        Map<String, Object> newAttributes = new java.util.HashMap<>();
        newAttributes.put("email", attributes.getEmail());
        newAttributes.put("name", attributes.getName());
        newAttributes.put("picture", attributes.getPicture());
        newAttributes.put("provider", attributes.getProvider().toString().toUpperCase());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())),
                newAttributes, // 새로 만든, 정제된 attributes를 사용합니다.
                "email"); // nameAttributeKey를 "email"로 지정하여 일관성을 유지합니다.
    }
}