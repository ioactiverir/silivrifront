package core.Cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class cache {

    // Credit of users has been charged
    public static final  LoadingCache<String, String> userCredit = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {

                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });


    /* userPhone, gifts value (balance)*/
    public static final  LoadingCache<String, String> userGifts = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    /* usrPhone, SMSCode */
    public static final  LoadingCache<String, String> sendCode = CacheBuilder.newBuilder()
            .expireAfterWrite(10,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });


    // keep sessions, map of userPhone & sessionID (cookie)
    public static final  LoadingCache<String, String> sessions = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });


    // contain users list, userPhone, usrPhone
    public static final  LoadingCache<String, String> userList = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });


    // map of view count, mediaID,viewCount
    public static final  LoadingCache<Long, Long> viewCount = CacheBuilder.newBuilder()
            .build(new CacheLoader<Long, Long>() {

                @Override
                public Long load(Long aLong) throws Exception {
                    return null;
                }
            });
    // map of mediaID, media source
    public static final  LoadingCache<Long,String> mediaLocation = CacheBuilder.newBuilder()
            .build(new CacheLoader<Long,String>() {

                @Override
                public String load(Long aLong) throws Exception {
                    return null;
                }
            });


    // map of authKey, QuzieId , every user have a unique quiz at the moment.
    public static final  LoadingCache<String,String> quizSession = CacheBuilder.newBuilder()
            .build(new CacheLoader<String,String>() {

                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });





}
