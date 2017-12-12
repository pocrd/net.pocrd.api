package net.pocrd.apigwtest.service;

import net.pocrd.apigwtest.api.LoadTestService;
import net.pocrd.apigwtest.entity.BadResponse;
import net.pocrd.apigwtest.entity.ComplexTestEntity;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.demo.api.DemoService;
import net.pocrd.demo.entity.DemoEntity;
import net.pocrd.responseEntity.DynamicEntity;
import net.pocrd.responseEntity.KeyValueList;
import net.pocrd.responseEntity.KeyValuePair;
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
    public ComplexTestEntity testDemoSayHello(String name) {
        DemoEntity de = demoService.sayHello(name);
        logger.info("say hello to " + name);
        ComplexTestEntity e = new ComplexTestEntity();
        e.charValue = '\n';
        e.strValue = de.name + "..." + de.id;
        if ("dynamicError".equals(name)) {
            KeyValueList ste = new KeyValueList();
            ste.keyValue = new ArrayList<KeyValuePair>(3);
            ste.keyValue.add(new KeyValuePair("a", "b"));
            ste.keyValue.add(new KeyValuePair("c", "d"));
            e.dynamicEntity = new DynamicEntity<KeyValueList>(ste);
        } else {
            SimpleTestEntity ste = new SimpleTestEntity();
            ste.intArray = new int[] { 1, 2, 3 };
            ste.strValue = "aabbcc";
            e.dynamicEntity = new DynamicEntity<SimpleTestEntity>(ste);
        }
        List<DynamicEntity> des = new ArrayList<DynamicEntity>(3);
        {
            SimpleTestEntity s = new SimpleTestEntity();
            s.intArray = new int[] { 4, 1, 4 };
            s.strValue = "kkkkkk";
            DynamicEntity de1 = new DynamicEntity(s);
            des.add(de1);

            if ("dynamicListError".equals(name)) {
                BadResponse b = new BadResponse("nonono");
                DynamicEntity de2 = new DynamicEntity(b);
                des.add(de2);
            }

            KeyValueList kvl = new KeyValueList();
            kvl.keyValue = new ArrayList<KeyValuePair>(2);
            kvl.keyValue.add(new KeyValuePair("x", "y"));
            kvl.keyValue.add(new KeyValuePair("n", "b"));
            DynamicEntity de3 = new DynamicEntity(kvl);
            des.add(de3);
        }
        e.dynamicEntityList = des;
        return e;
    }
}
