package net.pocrd.apigwtest.service;

import net.pocrd.apigwtest.api.LoadTestService;
import net.pocrd.apigwtest.entity.ApigwTestReturnCode;
import net.pocrd.apigwtest.entity.ComplexTestEntity;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.entity.ServiceException;
import net.pocrd.entity.ServiceRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LoadTestServiceImpl implements LoadTestService {
    private static final Logger logger = LoggerFactory.getLogger(ApiFunctionServiceImpl.class);

    @Override
    public SimpleTestEntity testSimpleTestEntityReturn(String strValue) throws ServiceException {
        try {
            SimpleTestEntity testEntity = new SimpleTestEntity();
            testEntity.strValue = strValue;
            return testEntity;
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw new ServiceException("api failed.", sre);
        } catch (Throwable t) {
            logger.error("api failed.", t);
            if (t instanceof ServiceException) {
                throw (ServiceException)t;
            } else {
                throw new ServiceException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
            }
        }
    }

    @Override
    public List<ComplexTestEntity> testComplexTestEntityListReturn() throws ServiceException {
        try {
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
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw new ServiceException("api failed.", sre);
        } catch (Throwable t) {
            logger.error("api failed.", t);
            if (t instanceof ServiceException) {
                throw (ServiceException)t;
            } else {
                throw new ServiceException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
            }
        }
    }

    @Override
    public int testNone(int intParam) throws ServiceException {
        try {
            return intParam;
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw new ServiceException("api failed.", sre);
        } catch (Throwable t) {
            logger.error("api failed.", t);
            if (t instanceof ServiceException) {
                throw (ServiceException)t;
            } else {
                throw new ServiceException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
            }
        }
    }

    @Override
    public int testRegisteredDevice(int intParam) throws ServiceException {
        try {
            return intParam;
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw new ServiceException("api failed.", sre);
        } catch (Throwable t) {
            logger.error("api failed.", t);
            if (t instanceof ServiceException) {
                throw (ServiceException)t;
            } else {
                throw new ServiceException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
            }
        }
    }

    @Override
    public int testUserLogin(int intParam) throws ServiceException {
        try {
            return intParam;
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw new ServiceException("api failed.", sre);
        } catch (Throwable t) {
            logger.error("api failed.", t);
            if (t instanceof ServiceException) {
                throw (ServiceException)t;
            } else {
                throw new ServiceException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
            }
        }
    }
}
