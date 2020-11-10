package com.ibicd.promote.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 自定义动态代理
 */
public class CustomDynamicProxy implements InvocationHandler {

    //被代理对象
    private Object target;

    public CustomDynamicProxy(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doSomethingBefore();
        Object invoke = method.invoke(target, args);
        doSomethingAfter();
        return invoke;
    }

    private void doSomethingBefore() {

        System.out.println("doSomethingBefore...");
    }

    private void doSomethingAfter() {
        System.out.println("doSomethingAfter...");
    }
}
