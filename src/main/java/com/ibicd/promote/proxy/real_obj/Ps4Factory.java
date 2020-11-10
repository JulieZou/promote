package com.ibicd.promote.proxy.real_obj;

import com.ibicd.promote.proxy.service.DesignFactory;
import com.ibicd.promote.proxy.service.GameFactory;

public class Ps4Factory implements GameFactory, DesignFactory {

    @Override
    public String make() {
        return "ps4";
    }

    @Override
    public String design() {
        return "ps4 design";
    }


}
