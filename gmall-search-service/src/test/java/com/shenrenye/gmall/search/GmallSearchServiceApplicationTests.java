package com.shenrenye.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shenrenye.gmall.beans.PmsSearchSkuInfo;
import com.shenrenye.gmall.beans.PmsSkuInfo;
import com.shenrenye.gmall.manage.user.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

	@Autowired
	private JestClient jestClient;
	@Reference
	private SkuService skuService;

	@Test
	public void testContextLoads() throws IOException {

		SearchSourceBuilder searchSource = new SearchSourceBuilder();
		searchSource.from(0);
		searchSource.size(10);

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","小米");
		boolQueryBuilder.must(matchQueryBuilder);

		TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id","61");
		boolQueryBuilder.filter(termQueryBuilder);

		searchSource.query(boolQueryBuilder);
		System.err.println(searchSource.toString());
		Search query = new Search.Builder(searchSource.toString()).addIndex("gmall0225").addType("PmsSearchSkuInfo").build();

		JestResult jr = jestClient.execute(query);
		System.out.println(jr);
	}

	@Test
	public void contextLoads() throws IOException {

		Search search = new Search.Builder("{\n" +
				"  \"query\": {\n" +
				"    \"match\": {\n" +
				"      \"skuName\": \"小米无敌天下\"\n" +
				"    }\n" +
				"  }\n" +
				"}").addIndex("gmall0225").addType("PmsSearchSkuInfo").build();

		JestResult jr = jestClient.execute(search);
		System.out.println(jr);
	}

	@Test
	public void testPmsSkuInfo() throws IOException {
		List<PmsSkuInfo> pmsSkuInfos =  skuService.getSkuInfoAll();

		List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

		for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
			PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
			// 将java的sku转化为es的sku
			BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
			String id = pmsSkuInfo.getId();
			long l = Long.parseLong(id);
			pmsSearchSkuInfo.setId(l);
			pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
		}


		for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
			Index index = new Index.Builder(pmsSearchSkuInfo).index("gmall0225").type("PmsSearchSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();

			JestResult execute = jestClient.execute(index);
		}
	}

}
