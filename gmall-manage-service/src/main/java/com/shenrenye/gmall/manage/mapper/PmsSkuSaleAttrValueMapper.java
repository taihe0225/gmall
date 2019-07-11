package com.shenrenye.gmall.manage.mapper;

import com.shenrenye.gmall.beans.PmsProductSaleAttr;
import com.shenrenye.gmall.beans.PmsSkuInfo;
import com.shenrenye.gmall.beans.PmsSkuSaleAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuSaleAttrValueMapper extends Mapper<PmsSkuSaleAttrValue>{

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckedBySkuId(@Param("spuId") String spuId,@Param("skuId") String skuId);

    List<PmsSkuInfo> selectCheckSkuBySpuId(@Param("spuId")String spuId);
}
