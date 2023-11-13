package com.example.guava.cache;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class CacheLoaderTest {
    @Test
    public void testBasic() throws Exception {
        // 定义 LoadingCache 缓存
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(10)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Employee>() {
                    @Override
                    public Employee load(String key) throws Exception {
                        return findEmployeeByName(key);
                    }
                });
        Employee employee = cache.get("Alex");
        assertThat(employee, notNullValue());
        // 休眠40s，缓存数据会在30s过期，因此又会 重新 获取。
        // TimeUnit.SECONDS.sleep(40);
        // 只会打印一条输出。没有从接口中获取，而是从Cache缓存中取值了。
        employee = cache.get("Alex");
        assertThat(employee, notNullValue());
    }

    private Employee findEmployeeByName(final String name) {
        System.out.println("The employee " + name + " is load from DB.");
        return new Employee(name, name, name);
    }



    // 创建 缓存 new创建
    private CacheLoader<String, Employee> createCacheLoader() {
        return new CacheLoader<String, Employee>() {
            @Override
            public Employee load(String key) {
                return findEmployeeByName(key);
            }
        };
    }

    // 创建 缓存 Function创建
//    private CacheLoader<String, Employee> createCacheLoader() {
//        return CacheLoader.from(key -> new Employee(key, key, key));
//    }




    @Test
    public void testEvictionBySize() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(3).build(createCacheLoader());
        cache.getUnchecked("Alex");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");

        assertThat(cache.size(), equalTo(3L));
        cache.getUnchecked("Susan");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getIfPresent("Alex"), nullValue());
        assertThat(cache.getIfPresent("Susan"), notNullValue());
    }



    /**
     * 测试 LRU清除策略
     * maximumWeight：设置最大总权重，不能和maximumSize一起使用；
     * concurrencyLevel：并发数为1，Segment=1，那么就不会将maximumWeight分为多个Segment。
     * weigher：设置权重函数；45/1=45。
     * （一般在 将要到达 最大权重时即会被回收）
     */
    @Test
    public void testEvictingByWeight() {
        Weigher<String, Employee> weigher = (key, employee) ->
            employee.getName().length() + employee.getEmpId().length() + employee.getDept().length();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .maximumWeight(45)
            .concurrencyLevel(1)
            .weigher(weigher)
            .build(createCacheLoader());
        // 3*5=15
        cache.getUnchecked("Gavin");
        // 3*5=15
        cache.getUnchecked("Kevin");
        // 3*5=15，一个Segment，15*3=45 达到最大权重。
        cache.getUnchecked("Allen");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getUnchecked("Gavin"), notNullValue());

        // 3*6=18，超出最大权重。根据最近最少使用原则，需要逐出两个旧的最少使用的元素（若只逐出1个，cache已使用权重=45，不足以放入18）
        cache.getUnchecked("123456");
        assertThat(cache.size(), equalTo(2L));
        assertThat(cache.getIfPresent("Kevin"), nullValue());
        assertThat(cache.getIfPresent("Allen"), nullValue());
    }




    @Test
    public void testEvictingByAccessTime() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .build(createCacheLoader());
        assertThat(cache.getUnchecked("Alex"), notNullValue());
        assertThat(cache.size(), equalTo(1L));

        // 休眠1秒，元素还会存在。
        TimeUnit.SECONDS.sleep(1);
        assertThat(cache.getIfPresent("Alex"), notNullValue());
        // 在休眠2秒，元素就会被逐出
        TimeUnit.SECONDS.sleep(2);
        assertThat(cache.getIfPresent("Alex"), nullValue());
    }



    // 设置 VM Options：-ea -XX:+PrintGCDetails。查看GC过程。
    // WeakReference 弱引用回收。
    @Test
    public void testWeakKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .weakValues().weakKeys().build(createCacheLoader());
        assertThat(cache.getUnchecked("Test"), notNullValue());
        // 手动触发GC，同步调用的异步方法，即不会被立即执行。因此休眠一会儿。
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        assertThat(cache.getIfPresent("Test"), nullValue());
        System.out.println(cache.size());
    }



    /**
     * Soft Reference 软引用,内存不足时，会被回收。
     * 设置缓存元素为 1M， JVM设置128M的堆内存（缓存不到128M时即会GC），进行测试
     * VM Option  -ea: -Xmx128M -Xms64M -XX:+PrintGCDetails
     */
    @Test
    public void testSoftKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .softValues().build(createCacheLoader());
        int i = 0;
        for (; ; ) {
            String key = "TestKey " + i;
            String value = "Employee " + i;
            cache.put(key, new Employee(value, value, value));
            System.out.println("The Employee [" + (i++) + "] is store into cache");
            TimeUnit.MILLISECONDS.sleep(600);
        }
    }




    // import com.google.common.base.Optional;
    @Test
    public void testLoadNullValueUseOptional() {
        LoadingCache<String, Optional<Employee>> cache = CacheBuilder.newBuilder().build(CacheLoader
            .from(key -> Objects.equals(key, "null") ? Optional.fromNullable(null) : Optional.fromNullable(new Employee(key, key, key))));
        assertThat(cache.getUnchecked("Test").get(), notNullValue());
        assertThat(cache.getUnchecked("null").orNull(), nullValue());
        Employee defaultValue = new Employee("Default", "Default", "Default");
        assertThat(defaultValue.getName(), equalTo("Default"));
    }




    @Test
    public void testCacheRemoveNotification() {
        // 缓存监听器，一旦移除元素，就执行 自定义的方法
        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                // 移除元素的原因
                assertThat(notification.getCause(), is(RemovalCause.SIZE));
                // 移除元素的 KEY 值
                assertThat(notification.getKey(), equalTo("Test"));
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            // 设置 缓存中最大元素个数
            .maximumSize(3)
            // 添加 监听器，监听 移除元素 的动作
            .removalListener(listener)
            .build(CacheLoader.from(String::toUpperCase));
        cache.getUnchecked("Test");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");
        cache.getUnchecked("Harry");
    }





    // 首次获取数据时，Cache中内容为空，因此命中率为0，错失率为 1；
    @Test
    public void testRecordStat() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .recordStats().build(CacheLoader.from(String::toUpperCase));
        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        CacheStats stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(0L));
        assertThat(stats.missCount(), equalTo(1L));

        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        // CacheStats对象是不可变的，因此需要重新创建。
        stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(1L));
        assertThat(stats.missCount(), equalTo(1L));
        System.out.println(stats.hitRate() + "," + stats.missRate());
    }




    @Test
    public void testCacheSpec() {
        String spec = "maximumSize=5,recordStats";
        CacheBuilderSpec builderSpec = CacheBuilderSpec.parse(spec);
        LoadingCache<String, String> cache =
            CacheBuilder.from(builderSpec).build(CacheLoader.from(String::toUpperCase));
    }









}
