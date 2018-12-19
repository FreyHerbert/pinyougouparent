package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			// 获取商家 id
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			// 设置商家 id
			goods.getGoods().setSellerId(sellerId);
			goodsService.add(goods);
			return getResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return getResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		// 当前商家 ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

		// 首先判断商品是否是该商品家的商品
		Goods oneGoods = goodsService.findOne(goods.getGoods().getId());
		if (!oneGoods.getGoods().getSellerId().equals(sellerId) ||
		!goods.getGoods().getSellerId().equals(sellerId)) {
			return new Result(false, "非法操作");
		}

		try {
			goodsService.update(goods);
			return getResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return getResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return getResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return getResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		// 获取商家Id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		return goodsService.findPage(goods, page, rows);		
	}

	private Result getResult(boolean success, String message) {
		return new Result(success, message);
	}
	
}
