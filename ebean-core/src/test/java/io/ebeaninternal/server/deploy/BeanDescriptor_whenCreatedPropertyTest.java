package io.ebeaninternal.server.deploy;

import io.ebean.BaseTestCase;
import io.ebean.DB;
import io.ebeaninternal.api.SpiEbeanServer;
import org.tests.model.basic.Customer;
import org.tests.model.basic.EBasic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class BeanDescriptor_whenCreatedPropertyTest extends BaseTestCase {


  @Test
  public void test() {

    SpiEbeanServer server = (SpiEbeanServer) DB.getDefault();

    BeanDescriptor<Customer> desc = server.getBeanDescriptor(Customer.class);

    BeanProperty whenCreatedProperty = desc.getWhenCreatedProperty();
    assertEquals("cretime", whenCreatedProperty.getDbColumn());

    BeanProperty whenModifiedProperty = desc.getWhenModifiedProperty();
    assertEquals("updtime", whenModifiedProperty.getDbColumn());


    BeanDescriptor<EBasic> eBasicDesc = server.getBeanDescriptor(EBasic.class);
    assertNull(eBasicDesc.getWhenCreatedProperty());
    assertNull(eBasicDesc.getWhenModifiedProperty());
  }
}
