package net.pocrd.apigw.common;

/**
 * 签名算法枚举
 */
public enum SignatureAlgorithm {
    MD5("md5"),
    SHA1("sha1"),
    HMAC("hmac"),
    RSA("rsa"),
    ECC("ecc");
    private String sigAlg;
    SignatureAlgorithm(String sigAlg) {
        this.sigAlg = sigAlg;
    }

    /**
     * 获取算法名
     *
     * @return algorithm
     */
    public String getAlgorithm() {
        return sigAlg;
    }
}
