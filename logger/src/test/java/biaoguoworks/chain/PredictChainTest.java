package biaoguoworks.chain;

import com.biaoguoworks.chain.AbsHandler;
import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.chain.DefaultChain;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predicate.IsPrinterLogContext;
import com.biaoguoworks.predicate.PredictType;
import com.biaoguoworks.predicate.handler.LogAllSqlPredicate;
import com.biaoguoworks.predicate.handler.SqIdPermitSetPredicate;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/26 15:03:16
 */
public class PredictChainTest {

    private Logger logger = LoggerFactory.getLogger(PredictChainTest.class);
    private Set<String> sqlIds = new HashSet<String>(){{
        add("selectUserIds");
        add("insertBatch");
    }};


    MappedStatement sqlIdContains = new MappedStatement.Builder(new Configuration(), "selectUserIds", parameterObject -> null, null).build();
    MappedStatement sqlIdNotContains = new MappedStatement.Builder(new Configuration(), "selectUserIdss", parameterObject -> null, null).build();


    Chain<IsPrinterLogContext> defaultChain = null;

    Config config = null;

    @Before
    public void beforeAll(){
        AbsHandler<IsPrinterLogContext> printerAllPredict = new LogAllSqlPredicate();
        AbsHandler<IsPrinterLogContext> sqlIdsPredict = new SqIdPermitSetPredicate();
        defaultChain = new DefaultChain<IsPrinterLogContext>()
                .add(printerAllPredict)
                .add(sqlIdsPredict);
         config = new Config();
         config.setLogger(logger);
    }

    /**
     * allOpen: true
     * sqlIdContains: false
     * predictType: OR
     * @throws Exception
     */
    @Test
    public void allOpenTrueTypeOR() throws Exception {
        config.setAllOpen(true);
        config.setPredictType(PredictType.OR);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertTrue(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 1);
    }

    /**
     * allOpen: false
     * sqlIdContains: true
     * predictType: OR
     * @throws Exception
     */
    @Test
    public void allOpenFalseTypeOrSqlIdContains() throws Exception {
        config.setAllOpen(false);
        config.setPredictType(PredictType.OR);
        config.setSqlIds(sqlIds);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        isPrinterLogContext.setMappedStatement(Optional.ofNullable(sqlIdContains));
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertTrue(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 2);

    }

    /**
     * allOpen: false
     * sqlIdContains: false
     * predictType: OR
     * @throws Exception
     */
    @Test
    public void allOpenFalseTypeOrSqlIdNotContains() throws Exception {
        config.setAllOpen(false);
        config.setPredictType(PredictType.OR);
        config.setSqlIds(sqlIds);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        isPrinterLogContext.setMappedStatement(Optional.ofNullable(sqlIdNotContains));
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertFalse(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 2);
    }


    /**
     * allOpen: true
     * sqlIdContains: false
     * predictType: AND
     * @throws Exception
     */
    @Test
    public void allOpenTrueTypeAnd() throws Exception {
        config.setAllOpen(true);
        config.setPredictType(PredictType.AND);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertFalse(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 2);
    }

    /**
     * allOpen: false
     * sqlIdContains: true
     * predictType: AND
     * @throws Exception
     */
    @Test
    public void allOpenFalseTypeAnd() throws Exception {
        config.setAllOpen(false);
        config.setSqlIds(sqlIds);
        config.setPredictType(PredictType.AND);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        isPrinterLogContext.setMappedStatement(Optional.ofNullable(sqlIdContains));
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertFalse(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 1);
    }


    /**
     * allOpen: true
     * sqlIdContains: true
     * predictType: AND
     * @throws Exception
     */
    @Test
    public void allOpenTrueTypeAndSQLIdContains() throws Exception {
        config.setAllOpen(true);
        config.setSqlIds(sqlIds);
        config.setPredictType(PredictType.AND);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        isPrinterLogContext.setMappedStatement(Optional.ofNullable(sqlIdContains));
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertTrue(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 2);
    }

    /**
     * allOpen: true
     * sqlIdContains: false
     * predictType: AND
     * @throws Exception
     */
    @Test
    public void allOpenTrueTypeAndSQlIDNotContains() throws Exception {
        config.setAllOpen(true);
        config.setSqlIds(sqlIds);
        config.setPredictType(PredictType.AND);
        IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext();
        isPrinterLogContext.setConfig(config);
        isPrinterLogContext.setMappedStatement(Optional.ofNullable(sqlIdNotContains));
        defaultChain.execChain(isPrinterLogContext);
        Assert.assertFalse(isPrinterLogContext.getPrinterLog());
        Assert.assertTrue(isPrinterLogContext.getPredictCount() == 2);
    }
}
