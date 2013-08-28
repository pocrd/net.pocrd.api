package net.pocrd.dao.entity;

public class ProductInfo {
    // 产品id
    private int    productId;
    // 产品名称
    private String productName;
    // 机构编号
    private int    corporationId;
    // 机构名称
    private String corporationName;
    // 贷款额度，元
    private int    ceiling;
    // 周期，月
    private int    cycle;
    // 利率%
    private double rate;
    // 产品分类，0：默认分类，1：消费贷款
    private int    categoryId;
    // 产品所属分类名称
    private String categoryName;
    // 年龄区间，格式18-100
    private String ageRange;
    // 工作年限最低要求
    private int floorWorkExp;
    // 贷款人所在行业要求
    private String industry;
    // 贷款人最低收入要求
    private int floorSalary;
    // 工作地点
    private String location;
    // 抵押类型
    private String mortgage;
    // 申请材料要求，这个要再考虑如何做关联
    private String material;
    // 产品推出时间,yyyy-mm-dd
    private String releaseTime;
    // 其他要求
    private String otherReq;
    //总利息
    private int interest;
    //月供
    private int monthlyPayment;
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getCorporationId() {
        return corporationId;
    }
    public void setCorporationId(int corporationId) {
        this.corporationId = corporationId;
    }
    public String getCorporationName() {
        return corporationName;
    }
    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }
    public int getCeiling() {
        return ceiling;
    }
    public void setCeiling(int ceiling) {
        this.ceiling = ceiling;
    }
    public int getCycle() {
        return cycle;
    }
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getAgeRange() {
        return ageRange;
    }
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }
    public int getFloorWorkExp() {
        return floorWorkExp;
    }
    public void setFloorWorkExp(int i) {
        this.floorWorkExp = i;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public int getFloorSalary() {
        return floorSalary;
    }
    public void setFloorSalary(int floorSalary) {
        this.floorSalary = floorSalary;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getMortgage() {
        return mortgage;
    }
    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }
    public String getReleaseTime() {
        return releaseTime;
    }
    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
    public String getOtherReq() {
        return otherReq;
    }
    public void setOtherReq(String otherReq) {
        this.otherReq = otherReq;
    }
    public int getInterest() {
        return interest;
    }
    public void setInterest(int interest) {
        this.interest = interest;
    }
    public int getMonthlyPayment() {
        return monthlyPayment;
    }
    public void setMonthlyPayment(int monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
