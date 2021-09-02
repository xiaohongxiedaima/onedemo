package com.xiaohongxiedaima.demo.javacore.objectheader;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.vm.VM;

/**
 * @author liusheng
 * @date 2021年05月25日 10:54 上午
 */
@Slf4j
public class VmInfoDemo {

    public static void main(String[] args) {
        log.info(VM.current().details());
    }

}
