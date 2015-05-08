package com.manydesigns.portofino.shiro;

import org.apache.shiro.authc.credential.DefaultPasswordService;

public class PortofinoPasswordService extends DefaultPasswordService {

    @Override
    protected void checkHashFormatDurability() {
        //Shhh, be quiet.
    }
}