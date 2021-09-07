package org.tests.query.other;

import io.ebean.BaseTestCase;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.Expression;
import io.ebean.Query;
import org.tests.model.basic.Customer;
import org.junit.jupiter.api.Test;

public class TestSubQueryBinding extends BaseTestCase {

  @Test
  public void test() {

    Database server = DB.getDefault();

    Query<Customer> someCustomerIds = server.find(Customer.class).select("id").where().lt("id", 5).query();

    Expression someCustIdsExpression = server.expressionFactory().in("id", someCustomerIds);

    Query<Customer> query = server.find(Customer.class).where().like("name", "Rob%").not(someCustIdsExpression).query();

    query.findList();

  }

}
