package net.pocrd.webapi.product;

import java.sql.SQLException;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.api.resp.ApiListProductInfo.Api_List_ProductInfo;
import net.pocrd.api.resp.ApiProductInfo.Api_ProductInfo;
import net.pocrd.dao.ProductDAO;
import net.pocrd.dao.entity.ProductInfo;
import net.pocrd.data.Page;
import net.pocrd.define.SecurityType;
//import net.pocrd.util.EvaluaterProvider;
import net.pocrd.util.SingletonUtil;

@ApiGroup("product")
public class GetProductList {
    private ProductDAO          productDao = SingletonUtil.getSingleton(ProductDAO.class);

    @HttpApi(name = "product.getProductList", desc = "获取产品列表", security = SecurityType.None)
    public Api_List_ProductInfo execute(
            @ApiParameter(required = true, name = "ceiling", desc = "额度，元") int ceiling,
            @ApiParameter(required = true, name = "cycle", desc = "周期，月") int cycle,
            @ApiParameter(required = false, name = "categoryId", desc = "贷款分类,0：默认分类", defaultValue = "0") int categoryId,
            @ApiParameter(required = false, name = "pageIndex", desc = "页码", defaultValue = "1") int pageIndex,
            @ApiParameter(required = false, name = "pageSize", desc = "页大小", defaultValue = "20") int pageSize,
            @ApiParameter(required = false, name = "orderby", desc = "排序依据，0:默认排序，1:月供排序，2:按照总利息", defaultValue = "0") int orderby) throws SQLException{
        // 与用户相关信息都从token中获取
        Api_List_ProductInfo.Builder resp = Api_List_ProductInfo.newBuilder();
        Page<ProductInfo> infopage = productDao.getProductInfoList(ceiling, cycle, categoryId, pageIndex, pageSize, orderby);
        if (infopage != null && infopage.getTotalCount() != 0) {
            Api_ProductInfo.Builder info = null;
            for (ProductInfo product : infopage.getList()) {
                info = Api_ProductInfo.newBuilder();
//                EvaluaterProvider.getEvaluater(Api_ProductInfo.Builder.class, ProductInfo.class).evaluate(info, product);
                if (product.getAgeRange() != null) info.setAgeRange(product.getAgeRange());
                info.setCategoryId(product.getCategoryId());
                info.setCategoryName(product.getCategoryName());
                info.setCeiling(product.getCeiling());
                info.setCorporationId(product.getCorporationId());
                info.setCorporationName(product.getCorporationName());
                info.setCycle(product.getCycle());
                info.setFloorSalary(product.getFloorSalary());
                info.setFloorWorkExp(product.getFloorWorkExp());
                if (product.getIndustry() != null) info.setIndustry(product.getIndustry());
                info.setInterest(product.getInterest());
                if (product.getLocation() != null) info.setLocation(product.getLocation());
                if (product.getMaterial() != null) info.setMaterial(product.getMaterial());
                info.setMonthlyPayamount(product.getMonthlyPayment());
                if (product.getMortgage() != null) info.setMortgage(product.getMortgage());
                if (product.getOtherReq() != null) info.setOtherReq(product.getOtherReq());
                info.setProductId(product.getProductId());
                info.setProductName(product.getProductName());
                info.setRate(product.getRate());
                if (product.getReleaseTime() != null) info.setReleaseTime(product.getReleaseTime());
                resp.addProductInfo(info.build());
            }
            resp.setTotalcount(infopage.getTotalCount());
            return resp.build();
        } else return null;
    }
}
