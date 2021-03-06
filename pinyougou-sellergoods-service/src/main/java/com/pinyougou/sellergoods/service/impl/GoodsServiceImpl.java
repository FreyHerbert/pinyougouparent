package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	/**
	 * tb_goods表 Mapper
	 */
	@Autowired
	private TbGoodsMapper goodsMapper;

	/**
	 *tb_goods_desc表 Mapper
	 */
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	/**
	 * tb_item表 Mapper
	 */
	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * tb_item_cat表 Mapper
	 */
	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 * tb_brand 表 Mapper
	 */
	@Autowired
	private TbBrandMapper brandMapper;

	/**
	 * tb_seller 表 Mapper
	 */
	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		TbGoods tbGoods = goods.getGoods();
		TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();

		// 修改状态为：未审核
		tbGoods.setAuditStatus("0");
		// 插入商品基本信息
		goodsMapper.insert(tbGoods);
		// 将 Goods 的 id 交给 GoodsDesc
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		// 插入商品扩展表数据
		goodsDescMapper.insert(tbGoodsDesc);

		saveItemList(goods);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		TbGoods tbGoods = goods.getGoods();
		TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();
		// 更新基本表数据
		goodsMapper.updateByPrimaryKey(tbGoods);
		// 更新扩展表数据
		goodsDescMapper.updateByPrimaryKey(tbGoodsDesc);

		// 删除原有的SKU列表数据
		TbItemExample itemExample = new TbItemExample();
		TbItemExample.Criteria itemExampleCriteria = itemExample.createCriteria();
		itemExampleCriteria.andGoodsIdEqualTo(tbGoods.getId());
		itemMapper.deleteByExample(itemExample);

		// 插入新的SKU列表数据
		saveItemList(goods);
	}

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		// 商品基本表
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		// 商品扩展表
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		// items 表
		TbItemExample itemExample = new TbItemExample();
		TbItemExample.Criteria criteria = itemExample.createCriteria();
		criteria.andGoodsIdEqualTo(tbGoods.getId());
		List<TbItem> tbItems = itemMapper.selectByExample(itemExample);
		goods.setItemList(tbItems);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	private void setItemValues(TbItem item, Goods goods) {
		TbGoods tbGoods = goods.getGoods();
		TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();

		// 3 级分类 Id
		item.setCategoryid(tbGoods.getCategory3Id());
		// 创建日期和更新日期
		item.setCreateTime(new Date());
		item.setUpdateTime(new Date());
		// 商品 Id
		item.setGoodsId(tbGoods.getId());
		// 商家 Id
		item.setSellerId(tbGoods.getSellerId());

		// 分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
		item.setCategory(itemCat.getName());

		// 商标名称
		TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
		item.setBrand(brand.getName());

		// 商家名称(店铺名称）
		TbSeller seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
		item.setSeller(seller.getNickName());

		// 图片
		List<Map> imageList = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
		if (imageList.size() > 0) {
			item.setImage((String) imageList.get(0).get("url"));
		}

		itemMapper.insert(item);
	}


	/**
	 * 插入 SKU 列表数据
	 */
	private void saveItemList(Goods goods) {
		TbGoods tbGoods = goods.getGoods();
		if ("1".equals(tbGoods.getIsEnableSpec())) {
			for (TbItem item : goods.getItemList()) {
				// 构建标题,SPU名称+规格选项
				// SPU名称
				String title = tbGoods.getGoodsName();
				Map<String, Object> map = JSON.parseObject(item.getSpec());
				for (String key : map.keySet()) {
					title += " " + map.get(key);
				}
				item.setTitle(title);

				setItemValues(item, goods);
			}
		} else {
			// 没有启动规格
			TbItem item = new TbItem();
			item.setTitle(tbGoods.getGoodsName());
			item.setPrice(tbGoods.getPrice());
			item.setNum(999);
			item.setStatus("1");
			item.setIsDefault("1");

			setItemValues(item, goods);
		}
	}
}
