package boot.service.impl;

import boot.domain.OperationResponse;
import boot.domain.order.Order;
import boot.domain.order.OrderStatus;
import boot.domain.order.PlaceOrderRequestVO;
import boot.mapper.order.OrderMapper;
import boot.service.OrderService;
import boot.service.PayService;
import boot.service.StorageService;
import boot.support.DataSourceKey;
import boot.support.DynamicDataSourceContextHolder;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HelloWoodes
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PayService payService;

    @Resource
    private StorageService storageService;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public OperationResponse placeOrder(PlaceOrderRequestVO placeOrderRequestVO) throws Exception {
        log.info("=============ORDER=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        log.info("当前 XID: {}", RootContext.getXID());

        Integer amount = 1;
        Integer price = placeOrderRequestVO.getPrice();

        Order order = Order.builder()
                           .userId(placeOrderRequestVO.getUserId())
                           .productId(placeOrderRequestVO.getProductId())
                           .status(OrderStatus.INIT)
                           .payAmount(price)
                           .build();

        Integer saveOrderRecord = orderMapper.saveOrder(order);

        log.info("保存订单{}", saveOrderRecord > 0 ? "成功" : "失败");

        // 扣减库存
        boolean operationStorageResult = storageService.reduceStock(placeOrderRequestVO.getProductId(), amount);

        // 扣减余额
        boolean operationBalanceResult = payService.reduceBalance(placeOrderRequestVO.getUserId(), price);

        log.info("=============ORDER=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);

        Integer updateOrderRecord = orderMapper.updateOrder(order.getId(), OrderStatus.SUCCESS);
        log.info("更新订单:{} {}", order.getId(), updateOrderRecord > 0 ? "成功" : "失败");

        return OperationResponse.builder()
                                .success(operationStorageResult && operationBalanceResult)
                                .build();
    }
}
