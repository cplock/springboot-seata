package boot.mapper.order;

import boot.domain.order.Order;
import boot.domain.order.OrderStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by jinjunzhu on 2020/8/5.
 */
@Mapper
public interface OrderMapper {

    /**
     * 保存订单
     *
     * @param order 订单
     * @return 影响行数
     */
    @Insert("INSERT INTO orders (user_id, product_id, pay_amount, status) VALUES (#{userId}, #{productId}, #{payAmount}, #{status})\n")
    Integer saveOrder(Order order);

    /**
     * 更新订单状态
     *
     * @param id     订单 ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}\n")
    Integer updateOrder(@Param("id") Integer id, @Param("status") OrderStatus status);
}
