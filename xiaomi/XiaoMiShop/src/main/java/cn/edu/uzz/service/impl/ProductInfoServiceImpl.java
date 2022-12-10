package cn.edu.uzz.service.impl;

import cn.edu.uzz.mapper.ProductInfoMapper;
import cn.edu.uzz.pojo.ProductInfo;
import cn.edu.uzz.pojo.ProductInfoExample;
import cn.edu.uzz.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname ProductInfoServiceImpl
 * @Description TODO
 * @Date 2022/11/25 14:13
 * @Created by Administrator
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    //在业务逻辑中，一定会有数据库访问层对象
    @Autowired
    private ProductInfoMapper infoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return infoMapper.selectByExample(new ProductInfoExample());
    }

    /**
     * 分页
     *
     * @param pageNum  当前页
     * @param pageSize 每页取几条数据
     * @return {@code PageInfo}
     */
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //使用PageHelper完成分页的设置
        PageHelper.startPage(pageNum,pageSize);
        //进行对PageInfo的数据封装
        //进行有条件查询操作，必须要创建一个ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按照当前主键的降序排序，新插入的数据显示在最上面
        //select * from product_info order by p_id desc
        example.setOrderByClause("p_id desc");
        //设置完排序后，获取排序后的商品集合
        List<ProductInfo> list = infoMapper.selectByExample(example);
        //将查询到的商品集合list封装到PageInfo中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
