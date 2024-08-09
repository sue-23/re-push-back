package org.shooong.push.domain.coupon.repository;

import org.shooong.push.domain.enumData.CouponCondition;
import org.shooong.push.global.exception.CouponConditionNotFoundException;
import org.shooong.push.global.exception.ErrorCode;
import org.shooong.push.global.exception.RedisOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;
//
//@Repository
//public class RedisRepository {
//
//    private static final String COUPON_TYPE_KEY = "coupon:time-attack:condition";
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//    private HashOperations<String, String, String> hashOperations;
//
//    @Autowired
//    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.hashOperations = redisTemplate.opsForHash();
//    }
//
//    public Long couponIssuedCount(Long couponId) {
//        try {
//            String issuedCountKey = COUPON_TYPE_KEY + couponId + ":issued:count";
//            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//            return valueOperations.increment(issuedCountKey);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to increment issued count for coupon ID: " + couponId, e);
//        }
//    }
//
//    public void issuedCancel(Long couponId) {
//        try {
//            String issuedCountKey = COUPON_TYPE_KEY + couponId + ":issued:count";
//            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//            valueOperations.decrement(issuedCountKey);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to decrement issued count for coupon ID: " + couponId, e);
//        }
//    }
//
//    public Long registerCouponUser(Long couponId, Long userId) {
//        try {
//            String userSetKey = COUPON_TYPE_KEY + couponId + ":users";
//            return redisTemplate.opsForSet().add(userSetKey, String.valueOf(userId));
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to register user ID: " + userId + " for coupon ID: " + couponId, e);
//        }
//    }
//
//    public void saveCouponCondition(String couponId, CouponCondition conditionKey, String conditionValue) {
//        try {
//            hashOperations.put(COUPON_TYPE_KEY + ":" + couponId, conditionKey.name(), conditionValue);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to save condition " + conditionKey.name() + " for coupon ID: " + couponId, e);
//        }
//    }
//
//    public String getCouponCondition(Long couponId, String conditionKey) {
//        try {
//            return hashOperations.get(COUPON_TYPE_KEY + ":" + couponId, conditionKey);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to get condition " + conditionKey + " for coupon ID: " + couponId, e);
//        }
//    }
//
//    public Map<String, String> getAllCouponConditions(Long couponId) {
//        try {
//            return hashOperations.entries(COUPON_TYPE_KEY + ":" + couponId);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to get all conditions for coupon ID: " + couponId, e);
//        }
//    }
//
//    public void deleteCouponConditions(Long couponId) {
//        try {
//            redisTemplate.delete(COUPON_TYPE_KEY + ":" + couponId);
//        } catch (Exception e) {
//            throw new RedisOperationException("Failed to delete conditions for coupon ID: " + couponId, e);
//        }
//    }
//}
@Repository
public class RedisRepository {

    private static final String COUPON_TYPE_KEY = "coupon:time-attack:condition";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public Long couponIssuedCount(Long couponId) {
        try {
            String issuedCountKey = COUPON_TYPE_KEY + couponId + ":issued:count";
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            return valueOperations.increment(issuedCountKey);
        } catch (Exception e) {
            throw new RedisOperationException("Failed to increment issued count for coupon ID: " + couponId, e);
        }
    }

    public void issuedCancel(Long couponId) {
        try {
            String issuedCountKey = COUPON_TYPE_KEY + couponId + ":issued:count";
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.decrement(issuedCountKey);
        } catch (Exception e) {
            throw new RedisOperationException("Failed to decrement issued count for coupon ID: " + couponId, e);
        }
    }

    public Long registerCouponUser(Long couponId, Long userId) {
        try {
            String userSetKey = COUPON_TYPE_KEY + couponId + ":users";
            return redisTemplate.opsForSet().add(userSetKey, String.valueOf(userId));
        } catch (Exception e) {
            throw new RedisOperationException("Failed to register user ID: " + userId + " for coupon ID: " + couponId, e);
        }
    }

    public void saveCouponCondition(String couponId, CouponCondition conditionKey, String conditionValue) {
        try {
            hashOperations.put(COUPON_TYPE_KEY + ":" + couponId, conditionKey.name(), conditionValue);
        } catch (Exception e) {
            throw new RedisOperationException("Failed to save condition " + conditionKey.name() + " for coupon ID: " + couponId, e);
        }
    }

    public String getCouponCondition(Long couponId, String conditionKey) {
        try {
            String condition = hashOperations.get(COUPON_TYPE_KEY + ":" + couponId, conditionKey);
            if (condition == null) {
//                throw new CouponConditionNotFoundException("Condition " + conditionKey + " not found for coupon ID: " + couponId);
                throw new CouponConditionNotFoundException(ErrorCode.COUPON_NOT_FOUND_CONDITION);
            }
            return condition;
        } catch (Exception e) {
            throw new RedisOperationException("Failed to get condition " + conditionKey + " for coupon ID: " + couponId, e);
        }
    }

    public Map<String, String> getAllCouponConditions(Long couponId) {
        try {
            return hashOperations.entries(COUPON_TYPE_KEY + ":" + couponId);
        } catch (Exception e) {
            throw new RedisOperationException("Failed to get all conditions for coupon ID: " + couponId, e);
        }
    }

    public void deleteCouponConditions(Long couponId) {
        try {
            redisTemplate.delete(COUPON_TYPE_KEY + ":" + couponId);
        } catch (Exception e) {
            throw new RedisOperationException("Failed to delete conditions for coupon ID: " + couponId, e);
        }
    }
}