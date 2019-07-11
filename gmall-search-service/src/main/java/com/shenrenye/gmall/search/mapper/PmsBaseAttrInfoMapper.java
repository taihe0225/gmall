package com.shenrenye.gmall.search.mapper;

import com.shenrenye.gmall.beans.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo>{

    List<PmsBaseAttrInfo> selectAttrValueByValueIds(@Param("valueIdsStr") String valueIdsStr);
}
