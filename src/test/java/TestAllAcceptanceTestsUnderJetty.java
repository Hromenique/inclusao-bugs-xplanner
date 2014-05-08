/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 15, 2006
 * Time: 4:33:02 PM
 */

import junit.framework.TestSuite;
import junit.framework.Test;

public class TestAllAcceptanceTestsUnderJetty extends TestSuite {
  public static Test suite() throws Exception {
    return new JettyTestDecorator(TestAllAcceptanceTests.suite());
  }
}