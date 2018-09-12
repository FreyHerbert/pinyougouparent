package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

/**
 * 品牌页面控制类
 * @author leiyu
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    /**
     * 品牌的服务接口，通过 dubbox 获取
     */
    @Reference
    private BrandService brandServic;

    /**
     * 查询出所有品牌的信息
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandServic.findAll();
    }

    /**
     * 品牌信息的分页操作
     * @param page 当前页
     * @param size 每页显示的条数
     * @return 返回分页的结果 [{"list":[{name:data},...]}, {"total": "number"}]
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int size) {
        return brandServic.findPage(page, size);
    }

    /**
     * 新增一条品牌信息
     * @param brand 品牌的 pojo 对象
     * @return 返回一个记录有状态的 Result 对象， 格式 {"success": "false|true" , "message":".."}
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            brandServic.add(brand);
            return new Result(true, "新增成功");
        } catch (RuntimeException  sqlE) {
            return new Result(false, "品牌重复");
        } catch (Exception e) {
            return new Result(false, "新增失败");
        }
    }

    /**
     * 根据 id 查询出品牌信息
     * @param id 所要查询的 id
     * @return 返回包含品牌信息的对象或null
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        TbBrand brand = brandServic.findOne(id);
        return brand;
    }

    /**
     * 更新品牌信息
     * @param brand 所要更新的对象
     * @return 返回一个记录有状态的 Result 对象， 格式 {"success": "false|true" , "message":".."}
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            brandServic.update(brand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            return new Result(false, "修改失败");
        }
    }

    /**
     * 根据 id 删除品牌信息
     * @param ids 这是个数组，删除数组中 id 所对应的品牌信息
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            brandServic.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand brand, int page, int size){
        return brandServic.findPage(brand, page, size);
    }

    @RequestMapping("/selectOptionList")
    public List<Map> getBrandService() {
        return brandServic.selectOptionList();
    }
}
