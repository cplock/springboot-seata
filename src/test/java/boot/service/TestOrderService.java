package boot.service;

import boot.domain.order.Order;
import boot.domain.order.OrderStatus;
import boot.mapper.order.OrderMapper;
import boot.support.AbstractSpringbootTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by jinjunzhu on 2020/8/6.
 */
public class TestOrderService extends AbstractSpringbootTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void testSave(){
        Order order = new Order();
        order.setPayAmount(100);
        order.setProductId(1L);
        order.setStatus(OrderStatus.SUCCESS);
        orderMapper.saveOrder(order);
    }
}
