package cn.edu.uzz.controller;

import cn.edu.uzz.pojo.ProductInfo;
import cn.edu.uzz.service.ProductInfoService;
import cn.edu.uzz.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Classname ProductInfoAction
 * @Description TODO
 * @Date 2022/11/25 14:20
 * @Created by Administrator
 */
@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //静态变量，每页五条数据
    private static final int PAGE_SIZE = 5;
    //在所有的控制层中，一定要有业务逻辑层对象
    @Autowired
    private ProductInfoService infoService;

    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        //获取所有的商品信息
        List<ProductInfo> infoList = infoService.getAll();
        //把商品信息通过request放入list中
        request.setAttribute("list",infoList);
        return "product";//转到product.jsp页面中
    }

    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info = infoService.splitPage(1, PAGE_SIZE);
        request.setAttribute("info",info);
        return "product";
    }

    /**
     * ajax分页处理，不需要返回值
     *
     * @param page    获取的第几页的数据，通过ajax传递的参数
     * @param session 会话
     */
    @ResponseBody//做分页是ajax请求，需要绕过ViewResolver
    @RequestMapping("/ajaxsplit")
    public void ajaxSplit(int page, HttpSession session){
        //获取到当前page页参数的页面数据，为PageInfo对象
        PageInfo info = infoService.splitPage(page, PAGE_SIZE);
        //处理完的数据会回到ajax的success中，然后读取info.list中的数据，
        //可以用session读取到指定页面的所有数据
        session.setAttribute("info",info);
    }

    /**
     * ajax上传图片并在div中回显
     *
     * @param pimage  pimage 专门进行当前上传文件流对象的接收，和input标签中的name属性一样，如果不一样，图片回传不了
     * @param request 请求
     * @return {@code Object} 返回json对象
     */
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //获取长传后，UUID加密后的文件名字
        //getOriginalFilename,获取图片的原始名字
        String saveFileName = FileNameUtil.getUUIDFileName()
                + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //获取项目中图片存放的路i纪念馆
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            //这里捕获下异常，有可能加载不到文件
            //转存文件
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
        }
        //import org.json.JSONObject;
        JSONObject object = new JSONObject();
        //封装图片的路径
        object.put("imgurl",saveFileName);
        return object.toString();
    }
}
