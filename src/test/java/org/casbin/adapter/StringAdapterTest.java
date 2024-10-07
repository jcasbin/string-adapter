package org.casbin.adapter;

import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.junit.Assert;
import org.junit.Test;

public class StringAdapterTest {

    @Test
    public void testKeyMatchRbac() {
        String conf = "[request_definition]\n" +
                "r = sub, obj, act\n\n" +
                "[policy_definition]\n" +
                "p = sub, obj, act\n\n" +
                "[role_definition]\n" +
                "g = _ , _\n\n" +
                "[policy_effect]\n" +
                "e = some(where (p.eft == allow))\n\n" +
                "[matchers]\n" +
                "m = g(r.sub, p.sub)  && keyMatch(r.obj, p.obj) && regexMatch(r.act, p.act)";

        String line = "p, alice, /alice_data/*, (GET)|(POST)\n" +
                "p, alice, /alice_data/resource1, POST\n" +
                "p, data_group_admin, /admin/*, POST\n" +
                "p, data_group_admin, /bob_data/*, POST\n" +
                "g, alice, data_group_admin";

        // ʹ�� StringAdapter ������������
        StringAdapter sa = new StringAdapter(line);

        // ���� Casbin Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // ���� Enforcer ������ģ�ͺͲ���
        Enforcer e = new Enforcer(md, sa);

        // ���� Enforce ����
        String sub = "alice"; // �û�
        String obj = "/alice_data/resource1"; // ��Դ
        String act = "POST"; // ����

        boolean result = e.enforce(sub, obj, act);

        // ���Խ��Ӧ��Ϊ true
        Assert.assertTrue(result);
    }

    @Test
    public void testStringRbac() {
        String conf = "[request_definition]\n" +
                "r = sub, obj, act\n\n" +
                "[policy_definition]\n" +
                "p = sub, obj, act\n\n" +
                "[role_definition]\n" +
                "g = _ , _\n\n" +
                "[policy_effect]\n" +
                "e = some(where (p.eft == allow))\n\n" +
                "[matchers]\n" +
                "m = g(r.sub, p.sub) && r.obj == p.obj && r.act == p.act";

        String line = "p, alice, data1, read\n" +
                "p, data_group_admin, data3, read\n" +
                "p, data_group_admin, data3, write\n" +
                "g, alice, data_group_admin";

        // ʹ�� StringAdapter ������������
        StringAdapter sa = new StringAdapter(line);

        // ���� Casbin Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // ���� Enforcer ������ģ�ͺͲ���
        Enforcer e = new Enforcer(md, sa);

        // ���� Enforce ����
        String sub = "alice"; // �û�
        String obj = "data1"; // ��Դ
        String act = "read";  // ����

        boolean result = e.enforce(sub, obj, act);

        // ���Խ��Ӧ��Ϊ true
        Assert.assertTrue(result);
    }
}
