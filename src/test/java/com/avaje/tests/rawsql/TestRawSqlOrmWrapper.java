package com.avaje.tests.rawsql;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.tests.model.basic.Customer;
import com.avaje.tests.model.basic.Order;
import com.avaje.tests.model.basic.OrderAggregate;
import com.avaje.tests.model.basic.ResetBasicData;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestRawSqlOrmWrapper extends BaseTestCase {

  @Test
  public void test() {

    ResetBasicData.reset();

    String sql = " select order_id, o.status, c.id, c.name, sum(d.order_qty*d.unit_price) as totalAmount"
      + " from o_order o"
      + " join o_customer c on c.id = o.kcustomer_id "
      + " join o_order_detail d on d.order_id = o.id "
      + " group by order_id, o.status, c.id, c.name ";

    RawSql rawSql = RawSqlBuilder.parse(sql).columnMapping("order_id", "order.id")
      .columnMapping("o.status", "order.status").columnMapping("c.id", "order.customer.id")
      .columnMapping("c.name", "order.customer.name")
      // .columnMapping("sum(d.order_qty*d.unit_price)", "totalAmount")
      .create();

    Query<OrderAggregate> query = Ebean.find(OrderAggregate.class);
    query.setRawSql(rawSql)
      // .fetch("order.details", new FetchConfig().query())
      .where().gt("order.id", 0).having().gt("totalAmount", 20);

    List<OrderAggregate> list = query.findList();
    Assert.assertNotNull(list);

    output(list);

    List<OrderAggregate> list2 = Ebean.find(OrderAggregate.class).setRawSql(rawSql)
      // .fetch("order.details", new FetchConfig().query())
      .where().gt("order.id", 2).having().gt("totalAmount", 10).findList();

    output(list2);

  }

  private void output(List<OrderAggregate> list) {

    for (OrderAggregate oa : list) {

      Order order = oa.getOrder();
      order.getId();
      order.getStatus();
      oa.getTotalAmount();

      Customer c = order.getCustomer();
      c.getId();
      c.getName();

      // invoke lazy loading as this property
      // has not populated originally
      // order.getOrderDate();
    }
  }
}
