package com.jiuzhang.seckill;

import com.jiuzhang.seckill.services.SeckillActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PayOrderTest {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Test
    public void payOrderTest(){
        seckillActivityService.payOrderProcess("");
    }
}
