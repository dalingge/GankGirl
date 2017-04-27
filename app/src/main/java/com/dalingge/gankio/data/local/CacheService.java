package com.dalingge.gankio.data.local;

import com.dalingge.gankio.data.model.ReadTypeBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * 缓存API接口
 * @LifeCache 设置缓存过期时间. 如果没有设置@LifeCache , 数据将被永久缓存理除非你使用了 EvictProvider, EvictDynamicKey or EvictDynamicKeyGroup .
 * EvictProvider可以明确地清理清理所有缓存数据.
 * EvictDynamicKey可以明确地清理指定的数据 DynamicKey.
 * EvictDynamicKeyGroup 允许明确地清理一组特定的数据. DynamicKeyGroup.
 * DynamicKey驱逐与一个特定的键使用EvictDynamicKey相关的数据。比如分页，排序或筛选要求
 * DynamicKeyGroup。驱逐一组与key关联的数据，使用EvictDynamicKeyGroup。比如分页，排序或筛选要求
 */
public interface CacheService {

    /**
     * 获取闲读分类信息
     * @param oRepos 缓存数据
     * @param userName
     * @param evictDynamicKey false使用缓存  true 加载数据不使用缓存
     * @return 数据
     */
   Observable<Reply<List<ReadTypeBean>>> getTypeList(Observable<List<ReadTypeBean>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);


    /**
     * 获取闲读分类下对应类别
     * 缓存时间 1天
     * @param oRepos  缓存数据
     * @param userName
     * @param evictDynamicKey false使用缓存  true 加载数据不使用缓存
     * @return 数据
     */
    @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
    Observable<Reply<ReadTypeBean>> getStackTypeList(Observable<ReadTypeBean> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

}
