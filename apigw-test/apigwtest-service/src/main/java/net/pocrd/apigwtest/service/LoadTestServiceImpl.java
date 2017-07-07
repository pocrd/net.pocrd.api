package net.pocrd.apigwtest.service;

import net.pocrd.apigwtest.api.LoadTestService;
import net.pocrd.apigwtest.entity.ComplexTestEntity;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.demo.api.DemoService;
import net.pocrd.demo.entity.DemoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LoadTestServiceImpl implements LoadTestService {
    private static final Logger logger = LoggerFactory.getLogger(ApiFunctionServiceImpl.class);

    @Autowired
    private DemoService demoService;

    @Override
    public SimpleTestEntity testSimpleTestEntityReturn(String strValue) {
        SimpleTestEntity testEntity = new SimpleTestEntity();
        testEntity.strValue = strValue;
        return testEntity;
    }

    @Override
    public List<ComplexTestEntity> testComplexTestEntityListReturn() {
        List<ComplexTestEntity> complexTestEntities = new ArrayList<ComplexTestEntity>();
        ComplexTestEntity complexTestEntity = new ComplexTestEntity();
        complexTestEntity.boolValue = true;
        complexTestEntity.byteValue = 1;
        complexTestEntity.charValue = 'a';
        complexTestEntity.doubleValue = 1.0;
        complexTestEntity.floatValue = 1;
        complexTestEntity.shortValue = 1;
        complexTestEntity.intValue = 1;
        complexTestEntity.longValue = 1L;
        SimpleTestEntity simpleTestEntity1 = new SimpleTestEntity();
        simpleTestEntity1.strValue = "simple test entity1";
        complexTestEntity.simpleTestEntity = simpleTestEntity1;
        List<SimpleTestEntity> simpleTestEntityList = new ArrayList<SimpleTestEntity>();
        SimpleTestEntity simpleTestEntity2 = new SimpleTestEntity();
        simpleTestEntity2.strValue = "simple test entity2";
        simpleTestEntityList.add(simpleTestEntity2);
        complexTestEntity.simpleTestEntityList = simpleTestEntityList;
        complexTestEntities.add(complexTestEntity);
        return complexTestEntities;
    }

    @Override
    public int testNone(int intParam) {
        return intParam;
    }

    @Override
    public int testRegisteredDevice(int intParam) {
        return intParam;
    }

    @Override
    public int testUserLogin(int intParam) {
        return intParam;
    }

    @Override
    public String testDemoSayHello(String name) {
        DemoEntity de = demoService.sayHello(name);
        return de.name + "..." + de.id;
    }
}
