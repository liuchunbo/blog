package com.example.mongodb;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

        @Test
        void contextLoads() {
            Book book = new Book();
            book.setId(143);
            book.setName("testMongoDB");
            book.setType("testMongoDB");
            book.setDescription("testMongoDB");
            //插入文档
            mongoTemplate.save(book);
            //插入文档
//            mongoTemplate.insert(book);

        }

    @Test
    void find() {
        //查所有
        List<Book> all = mongoTemplate.findAll(Book.class);
        System.out.println(all);

         //通过id查询
        Book book = mongoTemplate.findById(1, Book.class);
        System.out.println(book);

        //条件查询,查所有
        List<Book> r1 = mongoTemplate.find(new Query(), Book.class);

        //条件查询
        Query query=Query.query(Criteria.where("username").is("testMongoDB"));
        Query query1=Query.query(Criteria.where("type").isNull());
        List<Book> querys = mongoTemplate.find(query, Book.class);

        //or
        Criteria criteria = new Criteria();
        criteria.orOperator(
            Criteria.where("username").is("a"),
            Criteria.where("username").is("b"),
            Criteria.where("description").is("m")
        );
        mongoTemplate.find(Query.query(criteria), Book.class);

        Query query4=Query.query(Criteria.where("type").isNull().orOperator( Criteria.where("username").is("a")));
        mongoTemplate.find(query4, Book.class);



        //sort 排序
        Query query7 = new Query();
        query.with(Sort.by(Sort.Order.desc("age")));//desc 降序  asc 升序
        mongoTemplate.find(query, User.class);


        //skip limit 分页
        Query queryPage = new Query();
        queryPage.with(Sort.by(Sort.Order.desc("age")))   //desc 降序  asc 升序
            .skip(0) //起始条数
            .limit(4); //每页显示记录数
        mongoTemplate.find(queryPage, User.class);


        //count 总条数
        mongoTemplate.count(new Query(), User.class);

        //distinct 去重
        //参数 1:查询条件 参数 2: 去重字段  参数 3: 操作集合  参数 4: 返回类型
        mongoTemplate.findDistinct(new Query(), "name",
            User.class, String.class);

        //使用 json 字符串方式查询
        Query query8 = new BasicQuery(
            "{$or:[{name:'编程不良人'},{name:'徐凤年'}]}",
            "{name:0}");
        mongoTemplate.find(query, User.class);

    }


    @Test
    public void  testUpdate(){
        //1.更新条件
        Query query = Query.query(Criteria.where("age").is(23));
        //2.更新内容
        Update update = new Update();
        update.set("name","编程小陈陈");

        //单条更新
        mongoTemplate.updateFirst(query, update, User.class);
        //多条更新
        mongoTemplate.updateMulti(query, update, User.class);
        //更新插入
        mongoTemplate.upsert(query,update,User.class);

        //返回值均为 updateResult
        //System.out.println("匹配条数:" + updateResult.getMatchedCount());
        //System.out.println("修改条数:" + updateResult.getModifiedCount());
        //System.out.println("插入id_:" + updateResult.getUpsertedId());
    }

    @Test
    public void testDelete(){
        //删除所有
        mongoTemplate.remove(new Query(),User.class);
        //条件删除
        mongoTemplate.remove(
            Query.query(Criteria.where("name").is("编程不良人")),
            User.class
        );
    }





    @Test
    public void testCreatCollection() {
        // 集合是否才在,
        boolean productsTest = mongoTemplate.collectionExists("productsTest");

        //创建集合
        if (productsTest) {
            mongoTemplate.createCollection("productsTest");
        }


        //删除集合
        mongoTemplate.dropCollection("productsTest");

    }

}
