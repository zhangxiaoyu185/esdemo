package com.xiaoyu.lingdian.controller;

import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.entity.CoreCacheData;
import com.xiaoyu.lingdian.es.Esutil;
import com.xiaoyu.lingdian.service.CoreCacheDataService;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class EsController extends BaseController {

	@Autowired
	private CoreCacheDataService coreCacheDataService;

	@RequestMapping(value="/create/index", method= RequestMethod.POST)
	public void createIndex(HttpServletResponse response) {
		List<CoreCacheData> list = coreCacheDataService.findCoreCacheDataList();
		for(CoreCacheData doc : list){
			//把数据插入es
			Esutil.addIndex("jmzx","article", doc);
		}
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "创建成功!"),response);
	}

	@RequestMapping(value="/search", method = RequestMethod.GET)
	public void serachArticle(
			@RequestParam(value="keyWords",required = false) String keyWords,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
			HttpServletResponse response) {
		try {
			keyWords = new String(keyWords.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String, Object>();
		int count = 0;
		try {
			map = Esutil.search(keyWords,"jmzx","article",(pageNum-1)*pageSize, pageSize);
			count = Integer.parseInt(((Long) map.get("count")).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNum, pageSize, count);
		List<Map<String, Object>> articleList = (List<Map<String, Object>>)map.get("dataList");
		page.setResult(articleList);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "查询成功！", page), response);
	}
	
}
