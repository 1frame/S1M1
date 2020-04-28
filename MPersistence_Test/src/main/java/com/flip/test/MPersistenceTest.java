package com.flip.test;

import com.flip.dao.IUserDao;
import com.flip.io.Resources;
import com.flip.pojo.User;
import com.flip.sqlSession.SqlSession;
import com.flip.sqlSession.SqlSessionFactory;
import com.flip.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class MPersistenceTest {
    private SqlSession sqlSession;
    @Before
    public void test() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void insertTest(){
        //调用
        User user1 = new User(111, "琪亚娜");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int insert = userDao.insert(user1);
        System.out.println(insert);

        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test
    public void updateTest(){
        //调用
        User user1 = new User(111, "拉克丝");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int updateResult = userDao.update(user1);
        System.out.println(updateResult);

        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test
    public void delete(){
        User user1 = new User(111,"");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int deleteResult = userDao.delete(user1);
        System.out.println(deleteResult);

        //findAll
        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

}
