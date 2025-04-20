package com.learning.otp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Milan Rathod
 */
@Service
@Slf4j
public class OTPGeneratorService {

  private static final Integer EXPIRE_MIN = 5;

  private LoadingCache<String, String> otpCache;

  @PostConstruct
  public void init() {
    otpCache = CacheBuilder.newBuilder()
      .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
      .build(new CacheLoader<>() {
        @Override
        public String load(String s) throws Exception {
          return null;
        }
      });
  }

  /**
   * Method for generating OTP and put it in cache.
   *
   * @param key - cache key
   * @return cache value (generated OTP number)
   */
  public String generateOTP(String key) {
    final String otp = new DecimalFormat("000000")
      .format(new Random().nextInt(999999));
    otpCache.put(key, otp);
    return otp;
  }

  /**
   * Method for getting OTP value by key.
   *
   * @param key - target key
   * @return OTP value
   */
  public String getOPTByKey(String key) {
    return otpCache.getIfPresent(key);
  }

  /**
   * Method for removing key from cache.
   *
   * @param key - target key
   */
  public void clearOTPFromCache(String key) {
    otpCache.invalidate(key);
  }
}
