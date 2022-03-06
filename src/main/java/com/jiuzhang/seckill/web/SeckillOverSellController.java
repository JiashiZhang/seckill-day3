package com.jiuzhang.seckill.web;

import com.jiuzhang.seckill.services.SeckillActivityService;
import com.jiuzhang.seckill.services.SeckillOverSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeckillOverSellController {

    @Autowired
    private SeckillOverSellService seckillOverSellService;


    @Autowired
    private SeckillActivityService seckillActivityService;

    /**
     * 简单 处理抢购请求
     * @param seckillActivityId
     * @return
     */
    @ResponseBody
    @RequestMapping("/seckill123/{seckillActivityId}")
    public String  seckil(@PathVariable long seckillActivityId){
       return seckillOverSellService.processSeckill(seckillActivityId);
    }

    /**
     * 简单 处理抢购请求
     * @param seckillActivityId
     * @return
     */
    @ResponseBody
    @RequestMapping("/seckill/{seckillActivityId}")
    public String seckillCommodity(@PathVariable long seckillActivityId) {
        boolean stockValidateResult = seckillActivityService.seckillStockValidator(seckillActivityId);
        return stockValidateResult ? "恭喜你秒杀成功" : "商品已经售完，下次再来";
    }

}
