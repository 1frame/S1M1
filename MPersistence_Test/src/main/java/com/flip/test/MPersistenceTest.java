package com.flip.test;

import com.flip.dao.IUserDao;
import com.flip.io.Resources;
import com.flip.pojo.User;
import com.flip.sqlSession.SqlSession;
import com.flip.sqlSession.SqlSessionFactory;
import com.flip.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class MPersistenceTest {
    @Test
    public void test() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User(1, "张三");
        /*User userResult = sqlSession.selectOne("user.selectOne", user);
        System.out.println(userResult);*/

        /*List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }
    }

}
