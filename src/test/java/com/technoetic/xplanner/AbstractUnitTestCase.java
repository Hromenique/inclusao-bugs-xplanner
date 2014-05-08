package com.technoetic.xplanner;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.xplanner.events.EventManager;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.ObjectMother;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.easymock.EasyMockController;
import com.technoetic.xplanner.easymock.EasyMockHelper;

public abstract class AbstractUnitTestCase extends TestCase implements EasyMockController {

    private EasyMockHelper easymockHelper;
    protected MetaRepository mockMetaRepository;
    protected ObjectRepository mockObjectRepository;
    protected Session mockSession;
    protected Transaction mockTransaction;
    protected XPlannerTestSupport support;
    protected ObjectMother mom;
	protected EventManager eventBus;

    protected void setUp() throws Exception {
        super.setUp();
        easymockHelper = new EasyMockHelper();
        support = new XPlannerTestSupport();
        mom = new ObjectMother();
    }

    protected void setUpThreadSession() throws HibernateException {
        setUpThreadSession(true);
    }

    protected void setUpThreadSession(boolean expectCommit) throws HibernateException {
        mockSession = easymockHelper.createGlobalMock(Session.class);
        mockTransaction = easymockHelper.createNiceGlobalMock(Transaction.class);
        if (expectCommit) {
        	expect(mockSession.beginTransaction()).andReturn(mockTransaction);
        }
        ThreadSession.set(mockSession);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        ThreadSession.set(null);
    }

    protected final <T> T createLocalMock(Class<T> class1) {
    	return easymockHelper.createLocalMock(class1);
	}

    protected final <T> T createGlobalMock(Class<T> clazz) {
        return easymockHelper.createGlobalMock(clazz);
    }

    public void replay() {
        assertHelperPresent();
        easymockHelper.replayMocks();
    }

    private void assertHelperPresent() {
        TestCase.assertNotNull("no EasyMock helper: was super.setUp() called?", easymockHelper);
    }

    public void verify() {
        assertHelperPresent();
        easymockHelper.verifyMocks();
    }

    public void reset() {
        assertHelperPresent();
        easymockHelper.resetMocks();
    }

    protected void expectObjectRepositoryAccess(Class objectClass) {
        if (mockMetaRepository == null) {
            setUpRepositories();
        }
        expect(mockMetaRepository.getRepository(objectClass)).andReturn(mockObjectRepository).atLeastOnce();
    }

    protected void setUpRepositories() {
        mockMetaRepository = easymockHelper.createGlobalMock(MetaRepository.class);
        mockObjectRepository = easymockHelper.createGlobalMock(ObjectRepository.class);
        eventBus = easymockHelper.createGlobalMock(EventManager.class);
    }
}
