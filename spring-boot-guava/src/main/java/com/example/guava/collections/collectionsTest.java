package com.example.guava.collections;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.BoundType;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Creat by liuchunbo 2023/7/3
 */
public class collectionsTest {

    //    过滤：FluentIterable.filter();
    //    尾部添加：FluentIterable.append();
    //    条件全部满足：FluentIterable.allMatch();
    //    条件部分满足：FluentIterable.anyMatch();
    //    第一个满条件的元素：FluentIterable.firstMatch();
    //    获取首/尾元素：FluentIterable.first();
    //    获取前n个元素：FluentIterable.limit(3);
    //    元素拷贝：FluentIterable.copyInto();
    //    元素循环：FluentIterable.cycle();
    //    元素转换：FluentIterable.transform();
    //    元素拼接字符串：FluentIterable.join();

    @Test
    public void IntTest() {

        Integer i1 = 10;
        Integer i2 = 10;

        Integer i3 = 200;
        Integer i4 = 200;

        Integer i5= new Integer(10);
        Integer i6= new Integer(10);
        Integer i7= new Integer(200);
        Integer i8= new Integer(200);

        System.out.println(i1 == i2);
        System.out.println(i3 == i4);
        System.out.println(i5 == i1);
        System.out.println(i7 == i8);

        Collection a;


    }

    //笛卡尔积：
    @Test
    public void testCartesianProduct() {
        List<List<String>> result = Lists.cartesianProduct(Lists.newArrayList("1", "2"), Lists.newArrayList("A", "B"));
        System.out.println(result);
    }

    //元素转换：Lists.transform(list,function);
    @Test
    public void test() {
        List<String> result = Lists.newArrayList("Scala", "Java", "Python");
        Lists.transform(result, e -> e.toUpperCase()).forEach(System.out::println);
    }

    //顺序颠倒：Lists.reverse()
    @Test
    public void testReverse() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3");
        List<String> reverse = Lists.reverse(list);
        assertThat(Joiner.on(",").join(reverse), equalTo("3,2,1"));
    }

    //分组提高效率：Lists.partition()
    @Test
    public void testPartition() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3", "4");
        List<List<String>> result = Lists.partition(list, 3);
        System.out.println(result.get(0));
        System.out.println(result.get(1));
    }

    //生成n个子集：Sets.combinations(set, n);
    @Test
    public void testCombinations() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        Set<Set<Integer>> combinations = Sets.combinations(aSet, 2);
        System.out.println(combinations.size());
        combinations.forEach(System.out::println);
    }

    //差集（在A中不在B中）：Sets.difference(aSet, bSet);
    //交集（取出A、B共有）：Sets.intersection(aSet, bSet);
    //并集（A、B合并）：Sets.union(aSet, bSet);
    @Test
    public void testDifference() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4);
        HashSet<Integer> bSet = Sets.newHashSet(1, 3, 6);
        Sets.SetView<Integer> difference = Sets.difference(aSet, bSet);
        System.out.println(difference);
    }

    //Maps

    //Map的transform转换：Maps.transformValues(map,function);
    @Test
    public void testTransform() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> transform = Maps.transformValues(map, v -> v + "_transform");
        System.out.println(transform);
    }

    //filter过滤：Maps.filterKeys(map,function) 或 filterValues(map,function)
    //Java8过滤：lambda表达式
    @Test
    public void testFilter() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> filter = Maps.filterKeys(map, k -> Lists.newArrayList("2", "3").contains(k));
        System.out.println("Guava " + filter);
        // java 8: k->k.getKey()/k.getValue() 根据 key或value过滤
        Map<String, String> filter2 =
            map.entrySet().stream().filter(k -> Lists.newArrayList("2", "3").contains(k.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Java8 " + filter2);
    }

    //Multimaps： ArrayListMultimap，LinkedListMultimap。key相同，value不会被覆盖，而是按照数据结构存储成 数组或链表。
    @Test
    public void test12() {
        LinkedListMultimap<String, String> multimap = LinkedListMultimap.create();
        multimap.put("1", "1");
        multimap.put("1", "2");
        assertThat(multimap.size(), equalTo(2));
        System.out.println(multimap);
    }

    //BiMap: value不允许重复(抛异常：IllegalArgumentException)，key相同会覆盖value。

    //反转 Key-Value：HashBiMap.inverse();
    @Test
    public void test11() {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.put("5", "7");
        biMap.put("8", "12");
        System.out.println(biMap);
        BiMap<String, String> inverse = biMap.inverse();
        System.out.println(inverse);
    }

    //强制插入重复value，会覆盖key值：HashBiMap.forcePut();
    @Test
    public void testCreateAndForcePut() {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.forcePut("2", "2");
        System.out.println(biMap);
    }

    //Table
    //table的数据结构类似于：Map<String,Map<String,String>>
    @Test
    public void testTableBasic() {
        // Table<R, C, V>：Row行，Column列，Value值
        Table<String, String, String> table = HashBasedTable.create();
        table.put("Language", "Java", "8");
        table.put("Language", "Scala", "29.0");
        table.put("SQL", "Oracle", "12C");
        table.put("SQL", "MySql", "8.0");
        System.out.println(table);
        Map<String, String> rowSql = table.row("SQL");
        assertThat(rowSql.containsKey("MySql"), equalTo(true));
        System.out.println(rowSql.get("Oracle"));

        Set<Table.Cell<String, String, String>> cells = table.cellSet();
        System.out.println("【CellSet】= " + cells);
    }

    //0. 无穷区间：Range.all();
    //闭区间[0,9]：Range.closed(0, 9);
    //开区间(0,9)：Range.open(0, 9);
    //左闭右开[0,9)：Range.closedOpen(0, 9);
    //左开右闭(0,0]：Range.openClosed(0, 9);
    //————————————————

    //RangeMap 区间 对应 Object
    //例如：0~59 得C；60~79 得B；80~100 得A；
    @Test
    public void testMapRange() {
        // TreeMap有序，默认根据 ASCII码表顺序
        TreeMap<String, Integer> treeMap = Maps.newTreeMap();
        treeMap.put("Scala", 1);
        treeMap.put("Guava", 2);
        treeMap.put("Java", 3);
        treeMap.put("Kafka", 4);
        System.out.println("初始TreeMap： " + treeMap);

        NavigableMap<String, Integer> subMap = Maps.subMap(treeMap, Range.open("Guava", "Scala"));
        System.out.println("符合区间的TreeMap：" + subMap);

        Range.greaterThan(10); // 比10 大
        Range.downTo(10, BoundType.OPEN); // 最小是10，自定义开闭区间

        Range.lessThan(10); // 比10小
        Range.upTo(10, BoundType.OPEN); // 最大是10，自定义开闭区间

        Range.atLeast(10);
        Range.downTo(10, BoundType.CLOSED);

        Range.atMost(10);
        Range.upTo(10, BoundType.CLOSED);

    }

    //Immutable Collections 不可变集合
    @Test(expected = UnsupportedOperationException.class)
    public void testOf() {
        ImmutableList<Integer> list = ImmutableList.of(1, 2, 3);
        assertThat(list, notNullValue());
        // 不支持修改，excepted 处理 异常
        list.add(4);
        fail();
    }

    /**
     * ImmutableList.copyOf(array);
     */
    @Test
    public void testCopy() {
        Integer[] array = {1, 2, 3, 4, 5};
        ImmutableList<Integer> immutableList = ImmutableList.copyOf(array);
        System.out.println(immutableList);
    }

    /**
     * Builder方法创建
     */
    @Test
    public void testBuilder() {
        ImmutableList<Integer> immutableList =
            ImmutableList.<Integer>builder().add(1).add(2).add(3).addAll(Arrays.asList(4, 5)).build();
        System.out.println(immutableList);
    }

    @Test
    public void testImmutableMap() {
        ImmutableMap<String, String> map =
            ImmutableMap.<String, String>builder().put("SpringCloud", "2.5").put("Dubbo", "1.8").build();
        System.out.println(map);
    }

    /**
     * JDK 排序方法，按照自然顺讯排序：无法对有 null 元素的集合排序
     * Java8的排序方法
     */
    @Test
    public void testJdkSort() {
        List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
        Collections.sort(list);
        System.out.println(list);
    }

    /**
     * 集合中存在 null值 的排序方法。
     * Guava的排序方法
     */
    @Test
    public void testOrderNaturalByNullFirstOrNullLast() {
        List<Integer> ordered = Arrays.asList(1, 5, 6, 3, 7, 2, 8, 0);
        Collections.sort(ordered);
        System.out.println("是否按照自然顺序排序：" + Ordering.natural().isOrdered(ordered));

        List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
        Collections.sort(list, Ordering.natural().nullsFirst());
        System.out.println(list);

        Collections.sort(list, Ordering.natural().nullsLast());
        System.out.println(list);
    }

}
