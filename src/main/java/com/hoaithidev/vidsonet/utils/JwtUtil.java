package com.hoaithidev.vidsonet.utils;

import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.User;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Value("${jwt.signerKey}")
    String SecretKey;
    public String generateToken(User user) throws JOSEException {
        String email = user.getEmail();
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(email)
                .expirationTime(new Date(new Date().getTime() + 1000*60*60*24))
                .issueTime(new Date())
                .issuer("hoaithidev.com")
                .claim("scope", email.equals("admin@gmail.com")?"ADMIN":"USER")
                .claim("channelId", user.getEmail().equals("admin@gmail.com")?"admin":user.getChannel().getId())
                .build();

        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        JWSSigner signer = new MACSigner(SecretKey.getBytes());
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }


    public boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier verifier = new MACVerifier(SecretKey.getBytes());
        return jwsObject.verify(verifier);
    }
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Kiểm tra nếu claims null
            if (claims == null) {
                throw new IllegalArgumentException("Invalid JWT: Claims is null");
            }

            Date expirationTime = claims.getExpirationTime();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (ParseException e) {
            System.err.println("Invalid or malformed JWT: " + e.getMessage());
            return true; // Xem token như đã hết hạn nếu không parse được
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return true;
        }
    }
    public String getChannelId(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        return (String) claims.getClaim("channelId");
    }


    public String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
