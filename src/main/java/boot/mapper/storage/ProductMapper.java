package boot.mapper.storage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by jinjunzhu on 2020/8/5.
 */
@Mapper
public interface ProductMapper {
    /**
     * 获取库存
     *
     * @param productId 商品 ID
     * @return 库存
     */
    Integer getStock(@Param("productId") Long productId);


    /**
     * 扣减库存
     *
     * @param productId 商品 ID
     * @param amount    扣减数量
     * @return 影响记录行数
     */
    Integer reduceStock(@Param("productId") Long productId, @Param("amount") Integer amount);
}
