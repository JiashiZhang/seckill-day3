
package com.jiuzhang.seckill;

import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.mappers.SeckillActivityMapper;
import com.jiuzhang.seckill.db.mappers.SeckillCommodityMapper;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.db.po.SeckillCommodity;
import com.jiuzhang.seckill.services.SeckillOverSellService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class DaoTest {

    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Resource
    private SeckillCommodityMapper seckillCommodityMapper;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private SeckillOverSellService seckillOverSellService;


    @Test
    void SeckillActivityTest() {
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName("测试");
        seckillActivity.setCommodityId(999L);
        seckillActivity.setTotalStock(100L);
        seckillActivity.setSeckillPrice(new BigDecimal(99));
        seckillActivity.setActivityStatus(16);
        seckillActivityMapper.insert(seckillActivity);
        System.out.println("====>>>>" + seckillActivityMapper.selectByPrimaryKey(1L));
    }


    @Test
    void SeckillCommodityTest() {
        SeckillCommodity seckillCommodity = new SeckillCommodity();
        seckillCommodity.setCommodityName("11");
        seckillCommodity.setCommodityDesc("11");
        seckillCommodity.setCommodityPrice(100);
        seckillCommodityMapper.insert(seckillCommodity);
        seckillCommodityMapper.selectByPrimaryKey(11L);
        System.out.println("====>>>>" + seckillCommodityMapper.selectByPrimaryKey(1L));
    }

    @Test
    void setSeckillActivityQuery(){
        List<SeckillActivity> seckillActivitys = seckillActivityDao.querySeckillActivitysByStatus(0);
        System.out.println(seckillActivitys.size());
        seckillActivitys.stream().forEach(seckillActivity -> System.out.println(seckillActivity.toString()));
    }

    @Test
    void  overSellServiceTest(){
       for(int i=0;i<1000;i++){
           seckillOverSellService.processSeckill(11L);
       }
    }
}
