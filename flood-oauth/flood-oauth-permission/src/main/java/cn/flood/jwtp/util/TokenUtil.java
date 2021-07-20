package cn.flood.jwtp.util;

import cn.flood.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * Token工具类
 * <p>
 */
public class TokenUtil {
    public static final long DEFAULT_EXPIRE = 2 * 60 * 60;  // 默认token过期时长,单位秒
    public static final long DEFAULT_EXPIRE_REFRESH_TOKEN = 60 * 60 * 24 * 30 * 3;  // 默认refresh_token过期时长,单位秒

    /**
     * 生成token
     *
     * @param subject 载体
     * @return UserToken
     */
    public static UserToken buildToken(String subject) {
        return buildToken(subject, DEFAULT_EXPIRE);
    }

    /**
     * 生成token
     *
     * @param subject 载体
     * @param expire  token过期时间，单位秒
     * @return UserToken
     */
    public static UserToken buildToken(String subject, long expire) {
        return buildToken(subject, expire, DEFAULT_EXPIRE_REFRESH_TOKEN);
    }

    /**
     * 生成token
     *
     * @param subject  载体
     * @param expire   token过期时间，单位秒
     * @param rtExpire refresh_token过期时间，单位秒
     * @return
     */
    public static UserToken buildToken(String subject, long expire, long rtExpire) {
        return buildToken(subject, expire, rtExpire, getKey());
    }

    /**
     * 生成token
     *
     * @param subject  载体
     * @param expire   token过期时间，单位秒
     * @param rtExpire refresh_token过期时间，单位秒
     * @param key      密钥
     * @return UserToken
     */
    public static UserToken buildToken(String subject, Long expire, Long rtExpire, Key key) {
        return buildToken(subject, expire, rtExpire, key, true);
    }

    /**
     * 生成token
     *
     * @param subject  载体
     * @param expire   token过期时间，单位秒
     * @param rtExpire refresh_token过期时间，单位秒
     * @param key      密钥
     * @param needRt   是否生成refresh_token
     * @return UserToken
     */
    public static UserToken buildToken(String subject, Long expire, Long rtExpire, Key key, boolean needRt) {
        Date expireDate = new Date(new Date().getTime() + 1000 * expire);
        // 生成access_token
        String access_token = Jwts.builder().setSubject(subject).signWith(key).setExpiration(expireDate).compact();
        // 构建Token对象
        UserToken userToken = new UserToken();
        userToken.setUserId(subject);
        userToken.setAccessToken(access_token);
        userToken.setExpireTime(expireDate);
        // 生成refresh_token
        if (needRt) {
            Date refreshExpireDate = new Date(new Date().getTime() + 1000 * rtExpire);
            String refresh_token = Jwts.builder().setSubject(subject).signWith(key).setExpiration(refreshExpireDate).compact();
            userToken.setRefreshToken(refresh_token);
            userToken.setRefreshTokenExpireTime(refreshExpireDate);
        }
        return userToken;
    }

    /**
     * 解析token
     *
     * @param token  token
     * @param hexKey 16进制密钥
     * @return 载体
     */
    public static String parseToken(String token, String hexKey) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(parseHexKey(hexKey)).parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }

    /**
     * 生成密钥Key
     *
     * @return Key
     */
    public static Key getKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * 生成16进制的key
     *
     * @return hexKey
     */
    public static String getHexKey() {
        return getHexKey(getKey());
    }

    /**
     * 生成16进制的key
     *
     * @param key 密钥Key
     * @return hexKey
     */
    public static String getHexKey(Key key) {
        return Hex.encodeToString(key.getEncoded());
    }

    /**
     * 把16进制的key解析成Key
     *
     * @param hexKey 16进制key
     * @return Key
     */
    public static Key parseHexKey(String hexKey) {
        if (hexKey == null || hexKey.trim().isEmpty()) {
            return null;
        }
        SecretKey key = Keys.hmacShaKeyFor(Hex.decode(hexKey));
        return key;
    }

}
