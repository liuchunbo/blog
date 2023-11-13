//package com.example.guava.concurrency;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.google.common.util.concurrent.RateLimiter;
//import com.peng.sentinel.GaciException;
//import com.peng.sentinel.annotation.RLimit;
//import com.peng.sentinel.util.IPHelper;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.context.annotation.Configuration;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//
///**
// * 基于RateLimiter的限流 AOP
// */
//@Aspect
//@Configuration
//public class RLimitAspect {
//
//    // 这边根据IP来分不同的令牌桶, 这边设置的是每天自动清理缓存
//    // 使用Google的CacheBuilder本地缓存，后文会基于分布式Redis方案
//    // 缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
//    // maximumSize(long maximumSize)  设置缓存最大容量为maximumSize，超过maximumSize之后就会按照LRU最近虽少使用算法来移除缓存项
//    // maximumSize(1000) 设置缓存最大容量为1000
//    // expireAfterWrite(long duration, TimeUnit unit) duration 数字，TimeUnit 代表时间单位,
//    // expireAfterWrite(1, TimeUnit.DAYS) 设置写缓存后1天后过期
//    // build(xxx)方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
//
//    // 其它参数
//    // 设置并发级别为10，并发级别是指可以同时写缓存的线程数
//    // concurrencyLevel(10)
//    // 设置缓存的移除通知
//    // .removalListener(new RemovalListener<Object, Object>() {
//    //      @Override
//    //      public void onRemoval(RemovalNotification<Object, Object> notification) {
//    //          System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
//    //      }
//    // })
//    // new CacheLoader<String, RateLimiter> 本地缓存的存储
//    // RateLimiter.create(2) 根据KEY指定发出令牌数2
//
//
//    private static LoadingCache<String, RateLimiter> loadingCaches = CacheBuilder.newBuilder()
//            .maximumSize(1000)
//            .expireAfterWrite(1, TimeUnit.DAYS)
//            .build(new CacheLoader<String, RateLimiter>() {
//                @Override
//                public RateLimiter load(String key){
//                    // 新的IP初始化 每秒只发出10个令牌
////                    return RateLimiter.create(10);
//                    return RateLimiter.create(2);// 测试2个
//                }
//            });
//
//    // Service层切点  限流
//    @Pointcut("@annotation(com.peng.sentinel.annotation.RLimit)")
//    public void RLimitAspect() {
//
//    }
//
//    @Around("RLimitAspect()")
//    public  Object around(ProceedingJoinPoint joinPoint) throws Exception{
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        // 获取注解
//        RLimit limitAnnotation = method.getAnnotation(RLimit.class);
//        // 获取限流的类型
//        RLimit.LimitType limitType = limitAnnotation.limitType();
//        // 获取Key
//        String key = limitAnnotation.key();
//        Object obj;
//        try {
//            if(limitType.equals(RLimit.LimitType.IP)){
//                key = IPHelper.getIpAddr();
//            }
//            // 通过Key获取
//            RateLimiter rateLimiter = loadingCaches.get(key);
//     // tryAcquire(long timeout, TimeUnit unit)：判断是否可以在指定的时间内从ratelimiter获得一个许可，或者在超时期间内未获得许可的话，立即返回false。
//     // tryAcquire(int permits)：判断是否可以立即获取相应数量的许可。
//     // tryAcquire(）：判断是否可以立即获取许可。
//            // 未请求到limiter则立即返回false,就是未获取到令牌
//            Boolean flag = rateLimiter.tryAcquire();
//            if(flag){
//                // 放行
//                obj = joinPoint.proceed();
//            }else{
//                 throw  new Exception("宝宝，你访问的未免太频繁了") ;
//            }
//        } catch (Throwable e) {
//            throw  new Exception("宝宝，你访问的未免太频繁了") ;
//        }
//        return obj;
//    }
//}
