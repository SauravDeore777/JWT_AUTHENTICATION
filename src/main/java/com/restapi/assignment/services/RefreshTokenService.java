package com.restapi.assignment.services;

import com.restapi.assignment.entity.RefreshToken;
import com.restapi.assignment.entity.User;
import com.restapi.assignment.repository.RefreshTokenRepository;
import com.restapi.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    public long refreshTokenValidity=5*60*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;


    public RefreshToken createRefreshToken(String userName){
        User user= userRepository.findByEmail(userName).get();
        RefreshToken refreshtoken1=user.getRefreshToken();
        if(refreshtoken1==null){
       refreshtoken1= RefreshToken.builder().refreshToken(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(refreshTokenValidity))
                .user(user)
                .build();}
        else {
            refreshtoken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }
        user.setRefreshToken(refreshtoken1);
       refreshTokenRepository.save(refreshtoken1);
        return refreshtoken1;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
       RefreshToken refreshTokenOb= refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new RuntimeException("Given token doesn't exist"));
        if(refreshTokenOb.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshTokenOb);
            throw new RuntimeException("Refresh Token Expired");
        }
        return refreshTokenOb;
    }
}
