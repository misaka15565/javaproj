package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

import cn.sudoer.javaproj.bean.KeyPairBean;
import cn.sudoer.javaproj.entity.SysUser;

@Service
public class UserCookieService {
    private final KeyPairBean keyPairBean;
    // 有效时间1h
    private final long validTime = 3600000;

    public UserCookieService(KeyPairBean keyPairBean) {
        this.keyPairBean = keyPairBean;
    }

    public PublicKey getPublicKey() {
        return keyPairBean.getKeyPair().getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPairBean.getKeyPair().getPrivate();
    }

    public String getCookie(SysUser sysuser) {
        // 加密
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 计算过期时间
        long expire = now + validTime;
        // 拼接字符串
        String data = sysuser.getUsername() + "|" + expire;
        // 加密
        return keyPairBean.encrypt(data);
    }

    public boolean checkCookie(String cookie) {
        // 解密
        String data = keyPairBean.decrypt(cookie);
        if (data == null || data.isEmpty()) {
            return false;
        }
        // 解析数据
        String[] arr = data.split("\\|");
        if (arr.length != 2) {
            return false;
        }
        // 获取过期时间
        long expire = Long.parseLong(arr[1]);
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 判断是否过期
        return now < expire;
    }
    public String getUsernameFromCookie(String cookie){
        String data = keyPairBean.decrypt(cookie);
        if (data == null || data.isEmpty()) {
            return "";
        }
        String[] arr = data.split("\\|");
        if (arr.length != 2) {
            return "";
        }
        return arr[0];
    }
}
