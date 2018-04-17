package com.xiaoyu.lingdian.controller;

import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.tool.StringUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import com.xiaoyu.lingdian.entity.CoreCacheData;
import com.xiaoyu.lingdian.service.CoreCacheDataService;
import com.xiaoyu.lingdian.vo.CoreCacheDataVO;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/coreCacheData")
public class CoreCacheDataController extends BaseController {

	/**
	* 缓存数据表
	*/
	@Autowired
	private CoreCacheDataService coreCacheDataService;

	/**
	* 修改
	*
	* @param crcdaUuid 标识UUID
	* @param crcdaValue 缓存数据值
	* @return
	*/
	@RequestMapping(value="/update/coreCacheData", method=RequestMethod.POST)
	public void updateCoreCacheData (String crcdaUuid, String crcdaName, String crcdaValue, HttpServletResponse response) {
		logger.info("[CoreCacheDataController]:begin updateCoreCacheData");

		if (StringUtil.isEmpty(crcdaUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreCacheData coreCacheData = new CoreCacheData();
		coreCacheData.setCrcdaUuid(crcdaUuid);
		coreCacheData.setCrcdaName(crcdaName);
		coreCacheData.setCrcdaValue(crcdaValue);

		coreCacheDataService.updateCoreCacheData(coreCacheData);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改成功!"),response);
		logger.info("[CoreCacheDataController]:end updateCoreCacheData");

	}

	/**
	* 获取单个
	*
	* @param crcdaUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/views", method=RequestMethod.GET)
	public void viewsCoreCacheData (String crcdaUuid, HttpServletResponse response) {
		logger.info("[CoreCacheDataController]:begin viewsCoreCacheData");

		if (StringUtil.isEmpty(crcdaUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreCacheData coreCacheData = new CoreCacheData();
		coreCacheData.setCrcdaUuid(crcdaUuid);
		coreCacheData = coreCacheDataService.getCoreCacheData(coreCacheData);
		CoreCacheDataVO coreCacheDataVO = new CoreCacheDataVO();
		if(null != coreCacheData) {
			coreCacheDataVO.convertPOToVO(coreCacheData);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取单个信息成功", coreCacheDataVO), response);
		logger.info("[CoreCacheDataController]:end viewsCoreCacheData");

	}

	/**
	 * 缓存列表
	 *
	 * @param response
	 */
	@RequestMapping(value = "/find/all", method = RequestMethod.GET)
	public void findAll(HttpServletResponse response) {
		List<CoreCacheData> list = coreCacheDataService.findCoreCacheDataList();
		List<CoreCacheDataVO> functionVOs = new ArrayList<CoreCacheDataVO>();
		CoreCacheDataVO functionVO;
		for (CoreCacheData coreFunction : list) {
			functionVO = new CoreCacheDataVO();
			functionVO.convertPOToVO(coreFunction);
			functionVOs.add(functionVO);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取缓存列表成功！", functionVOs), response);
	}

	/**
	 * 缓存分页列表
	 *
	 * @param pageNum 页码
	 * @param pageSize 页数
	 * @param response
	 */
	@RequestMapping(value = "/find/page", method = RequestMethod.GET)
	public void findPage(String crcdaName, String crcdaValue, Integer pageNum, Integer pageSize, HttpServletResponse response) {
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 20;
		}
		Page<CoreCacheData> page = coreCacheDataService.findCoreCacheDataPage(crcdaName, crcdaValue, pageNum, pageSize);
		Page<CoreCacheDataVO> pageVO = new Page<CoreCacheDataVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreCacheDataVO> vos = new ArrayList<CoreCacheDataVO>();
		CoreCacheDataVO vo;
		for (CoreCacheData coreFunction : page.getResult()) {
			vo = new CoreCacheDataVO();
			vo.convertPOToVO(coreFunction);
			vos.add(vo);
		}
		pageVO.setResult(vos);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取缓存分页列表成功！", pageVO), response);
	}
}