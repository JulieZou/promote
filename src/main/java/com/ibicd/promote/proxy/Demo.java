package com.ibicd.promote.proxy;

import com.ibicd.promote.proxy.real_obj.Ps4Factory;
import com.ibicd.promote.proxy.service.GameFactory;

/**
 * 以动态代理方式的
 */
public class Demo {

    public static void main(String[] args) {
        Ps4Factory factory = new Ps4Factory();
        CustomDynamicProxy proxy = new CustomDynamicProxy(factory);

        //获取的代理一定真实对象实现的接口
        GameFactory proxy1 = (GameFactory) proxy.getProxy();
        System.out.println(proxy1.make());
    }
}
