package com.technoetic.xplanner.db;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.technoetic.xplanner.actions.SearchResultAuthorizationPredicate;
import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.domain.repository.RepositoryException;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Dec 14, 2004
 * Time: 2:58:40 PM
 */
public class ContentSearchHelper {
    protected String searchCriteria;
    private List searchResults;
    private Class[] searchableClasses = new Class[]{Project.class,
                                                    UserStory.class,
                                                    Task.class,
                                                    Note.class,
                                                    Iteration.class};
    private SearchResultFactory searchResultFactory;
    private SearchContentQuery query;

    public ContentSearchHelper(SearchResultFactory searchResultFactory,
                               SearchContentQuery query) {
        this.searchResultFactory = searchResultFactory;
        this.query = query;
    }

    public List getSearchResults() {
        return searchResults;
    }

    public void search(String searchCriteria, int userId, int restrictedProjectId) throws RepositoryException {
        this.searchCriteria = searchCriteria;
        query.setRestrictedProjectId(restrictedProjectId);
        List results = findMatchingResults(searchCriteria);
        excludeResultsBasedOnUserPermissions(results, userId);
        searchResults = results;
    }

    private List findMatchingResults(String searchCriteria) throws RepositoryException {
        List results = new LinkedList();
        for (int i = 0; i < searchableClasses.length; i++) {
            convertMatchesToResults(findMatchingObjects(searchableClasses[i], searchCriteria), results);
        }
        return results;
    }

    protected List findObjectsMatching(Class[] searchableClasses, String searchCriteria)
        throws RepositoryException {
        List results = new LinkedList();
        for (int i = 0; i < searchableClasses.length; i++) {
            convertMatchesToResults(findMatchingObjects(searchableClasses[i], searchCriteria), results);
        }
        return results;
    }

    private List findMatchingObjects(Class objectClass, String searchCriteria)
        throws RepositoryException {
        return query.findWhereNameOrDescriptionContains(searchCriteria,objectClass);
    }

    private void excludeResultsBasedOnUserPermissions(List results, int userId) {
        Predicate predicate = getAuthorizationPredicate(userId);
        CollectionUtils.filter(results, predicate);
    }

    protected Predicate getAuthorizationPredicate(int userId)
    {
        return new SearchResultAuthorizationPredicate(userId);
    }

    private void convertMatchesToResults(List matches, List results) {
        for (Iterator iterator = matches.iterator(); iterator.hasNext();) {
           try {
              results.add(searchResultFactory.convertObjectToSearchResult((Nameable) iterator.next(), searchCriteria));
           }
           catch (Exception ex){
           }
        }
    }

}
