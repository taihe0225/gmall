package com.shenrenye.gmall.manage.user.service;

import com.shenrenye.gmall.beans.PmsBaseAttrInfo;
import com.shenrenye.gmall.beans.PmsSearchParam;
import com.shenrenye.gmall.beans.PmsSearchSkuInfo;

import java.util.HashSet;
import java.util.List;

public interface SearchService {

    List<PmsSearchSkuInfo> search(PmsSearchParam pmsSearchParam);

    List<PmsBaseAttrInfo> getAttrValueByValueIds(HashSet<String> valueIdSet);
}
