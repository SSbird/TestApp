package com.example.testapp.utils;

// 本地和网络服务器的前缀
public enum Web {
    PREFIX_LOCAL("http://47.98.151.151:8090/");
//    PREFIX_LOCAL("http://10.0.2.2:8090/");    本地
//    PREFIX_SERVER("http://47.98.151.151:8090/");    阿里云服务器

    private String val;

    public String val() {
        return val;
    }

    Web(String val) {
        this.val = val;
    }
}


