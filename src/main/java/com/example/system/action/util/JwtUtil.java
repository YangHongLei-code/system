package com.example.system.action.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "your-secret-key";

    // 生成JWT令牌
    public static String generateToken(String claims) {
        return JWT.create()
                .withClaim("data", claims) // 添加自定义数据
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 设置过期时间为1小时
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 使用HMAC256算法签名
    }
    // 验证JWT令牌
    public static boolean verifyToken(String token) {

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token); // 验证令牌
            return true;

    }


    // 验证并解析JWT令牌
    public static String parseToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token); // 验证令牌
        return decodedJWT.getClaim("data").asString(); // 提取自定义数据
    }
}