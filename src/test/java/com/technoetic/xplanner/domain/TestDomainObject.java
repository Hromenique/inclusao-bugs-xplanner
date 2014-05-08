package com.technoetic.xplanner.domain;

import junit.framework.TestCase;

public class TestDomainObject extends TestCase
{
   DomainObject domainObject;


   public void testEquals() throws Exception
   {
      DomainObject object1 = new DummyDomainObject();
      DomainObject object2 = new DummyDomainObject();
      assertFalse(object1.equals(object2));
   }

}