package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.hibernate.Hibernate;

import org.apache.struts.upload.FormFile;
import org.easymock.ArgumentsMatcher;
import org.easymock.Capture;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.repository.DuplicateUserIdException;
import com.technoetic.xplanner.forms.ImportPeopleForm;
import com.technoetic.xplanner.forms.PeopleImportResult;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.util.InputStreamFormFile;

public class TestImportPeopleAction extends AbstractActionTestCase
{
    private ImportPeopleForm mockImportPeopleForm;
    private ImportPeopleAction importPeopleAction;
    private MockControl mockEditPersonHelperControl;
    private EditPersonHelper mockEditPersonHelper;
    private FormFile testFormFile;
    
    protected void setUp() throws Exception
    {
        action = importPeopleAction = new ImportPeopleAction();
        mockEditPersonHelperControl = MockClassControl.createControl(EditPersonHelper.class);
        mockEditPersonHelper = (EditPersonHelper) mockEditPersonHelperControl.getMock();
        super.setUp();
        mockImportPeopleForm = new ImportPeopleForm();
        mockImportPeopleForm.reset(support.mapping, support.request);
        support.form = mockImportPeopleForm;
        importPeopleAction.setServlet(support.actionServlet);
        support.setForward(AbstractAction.TYPE_KEY, Person.class.getName());
        importPeopleAction.setMetaRepository(mockMetaRepository);
        importPeopleAction.setEditPersonHelper(mockEditPersonHelper);
        mockImportPeopleForm.setAction(EditObjectAction.UPDATE_ACTION);
    }


    protected void tearDown() throws Exception
    {
        SystemAuthorizer.set(null);
        super.tearDown();
    }

    public void testImport_Success() throws Exception
    {
        setUpImportFile("importtestperson1,Import test person 1,tester1@sabre.com,MP,23456");
        Person person = new Person("importtestperson1");
        mockImportPeopleForm.setFormFile(testFormFile);
        expectObjectRepositoryAccess(Person.class);
        
		Capture<Person> expectedPerson = new Capture<Person>();
		expect(mockObjectRepository.insert(capture(expectedPerson))).andReturn(0);
        replay();
        support.executeAction(importPeopleAction);
        assertEquals(person.getId(), expectedPerson.getValue().getId());
        assertEquals("Wrong import status ", "people.import.status.success",
                     ((PeopleImportResult) mockImportPeopleForm.getResults().get(0)).getStatus());
        verify();
    }

    public void testImport_PersonAlreadyExists() throws Exception
    {
        Person person = new Person("importtestperson1");
        setUpImportFile("importtestperson1,Import test person 1,tester1@sabre.com,MP,23456");
        mockImportPeopleForm.setFormFile(testFormFile);
        expect(mockMetaRepository.getRepository(Person.class)).andReturn(mockObjectRepository);
		Capture<Person> expectedPerson = new Capture<Person>();
        expect(mockObjectRepository.insert(capture(expectedPerson))).andThrow(new DuplicateUserIdException());
        replay();
        support.executeAction(importPeopleAction);
        assertEquals("Wrong import status ", "people.import.status.userId_exists",
                     ((PeopleImportResult) mockImportPeopleForm.getResults().get(0)).getStatus());
    }

    public void testImport_NoUserId() throws Exception
    {
        setUpImportFile(",Import test person 1,tester1@sabre.com,MP,23456");
        mockImportPeopleForm.setFormFile(testFormFile);
        expect(mockSession.find("from person in class " + Person.class + " where person.userId = ?",
                         "importtestperson1", Hibernate.STRING)).andReturn(new ArrayList());
        replay();
        support.executeAction(importPeopleAction);
        assertEquals("Wrong import status ", "people.import.status.empty_userId",
                     ((PeopleImportResult) mockImportPeopleForm.getResults().get(0)).getStatus());
    }

    public void testImport_WrongEntryFormat() throws Exception
    {
        setUpImportFile(",Import test person 1,");
        mockImportPeopleForm.setFormFile(testFormFile);
        expect(mockSession.find("from person in class " + Person.class + " where person.userId = ?",
                         "importtestperson1", Hibernate.STRING)).andReturn(new ArrayList());
        replay();
        support.executeAction(importPeopleAction);
        assertEquals("Wrong import status ", "people.import.status.wrong_entry_format",
                     ((PeopleImportResult) mockImportPeopleForm.getResults().get(0)).getStatus());
    }


    private void setUpImportFile(String textToImport) throws IOException
    {
        testFormFile =
            new InputStreamFormFile(new ByteArrayInputStream(textToImport.getBytes()));
        testFormFile.setFileName("import.csv");
        testFormFile.setFileSize(textToImport.length());
    }

}
