package com.technoetic.xplanner.export;

import com.tapsterrock.mpx.MPXDuration;
import com.tapsterrock.mpx.MPXException;
import com.tapsterrock.mpx.MPXFile;
import com.tapsterrock.mpx.ProjectHeader;
import com.tapsterrock.mpx.Resource;
import com.tapsterrock.mpx.Task;
import com.tapsterrock.mpx.TimeUnit;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class MpxExporter implements Exporter {
    public void initializeHeaders(HttpServletResponse response) {
        response.setHeader("Content-type", "application/mpx");
        response.setHeader("Content-disposition", "inline; filename=export.mpx");
    }

    public byte[] export(Session session, Object object) throws ExportException {
        try {
            MPXFile file = new MPXFile();
            file.setAutoTaskID(true);
            file.setAutoTaskUniqueID(true);
            file.setAutoResourceID(true);
            file.setAutoResourceUniqueID(true);
            file.setAutoOutlineLevel(true);
            file.setAutoOutlineNumber(true);
            file.setAutoWBS(true);
            // Add a default calendar called "Standard"
//            file.addDefaultBaseCalendar();

            ResourceRegistry resourceRegistry = new ResourceRegistry(session.find("from person in class " + Person.class), file);

            if (object instanceof Project) {
                exportProject(file, (Project) object, resourceRegistry);
            } else if (object instanceof Iteration) {
                exportIteration(file, null, (Iteration) object, resourceRegistry);
            }

            ByteArrayOutputStream data = new ByteArrayOutputStream();
            file.write(data);
            return data.toByteArray();
        } catch (Exception e) {
            throw new ExportException("exception during export", e);
        }
    }

    private String filterString(String s) {
        return s.replaceAll("\r", "");
    }

    private void exportProject(MPXFile file, Project project, ResourceRegistry resources) throws MPXException {
        ProjectHeader header = file.getProjectHeader();
        Task projectLevelTask = file.addTask();
        header.setProjectTitle(project.getName());
        projectLevelTask.setName(project.getName());
        if (!StringUtils.isEmpty(project.getDescription())) {
            projectLevelTask.setNotes(filterString(project.getDescription()));
        }

        long earliestStartTime = Long.MAX_VALUE;
        List iterations = new ArrayList(project.getIterations());
        Collections.sort(iterations, new Comparator() {
            public int compare(Object o1, Object o2) {
                Iteration i1 = (Iteration) o1;
                Iteration i2 = (Iteration) o2;
                return i1.getStartDate().compareTo(i2.getStartDate());
            }
        });
        for (Iterator iterator = iterations.iterator(); iterator.hasNext();) {
            Iteration iteration = (Iteration) iterator.next();
            exportIteration(file, projectLevelTask, iteration, resources);
            if (iteration.getStartDate().getTime() < earliestStartTime) {
                earliestStartTime = iteration.getStartDate().getTime();
            }
        }

        if (earliestStartTime < Long.MAX_VALUE) {
            header.setStartDate(new Date(earliestStartTime));
        } else {
            header.setStartDate(new Date());
        }
    }

    private Task exportIteration(MPXFile file, Task projectLevelTask, Iteration iteration, ResourceRegistry resources) throws MPXException {
        Task iterationLevelTask = null;
        if (projectLevelTask != null) {
            iterationLevelTask = projectLevelTask.addTask();
        } else {
            iterationLevelTask = file.addTask();
            file.getProjectHeader().setStartDate(iteration.getStartDate());
            file.getProjectHeader().setProjectTitle(iteration.getName());
        }
        iterationLevelTask.setName(iteration.getName());
        iterationLevelTask.setStart(iteration.getStartDate());
        iterationLevelTask.setFinish(iteration.getEndDate());
        if (StringUtils.isNotEmpty(iteration.getDescription())) {
            iterationLevelTask.setNotes(filterString(iteration.getDescription()));
        }
        for (Iterator iterator = iteration.getUserStories().iterator(); iterator.hasNext();) {
            UserStory userStory = (UserStory) iterator.next();
            exportUserStory(iterationLevelTask, userStory, resources);
        }
        return iterationLevelTask;
    }

    protected void exportUserStory(Task iterationLevelTask, UserStory userStory, ResourceRegistry resources) throws MPXException {
        Task storyLevelTask = iterationLevelTask.addTask();
        storyLevelTask.setName(userStory.getName());
        if (StringUtils.isNotEmpty(userStory.getDescription())) {
            storyLevelTask.setNotes(filterString(userStory.getDescription()));
        }
        long earliestTaskStartTime = Long.MAX_VALUE;
        Collection storyTasks = userStory.getTasks();
        if (storyTasks.size() > 0) {
            storyLevelTask.setWork(new MPXDuration(userStory.getTaskBasedEstimatedHours(), TimeUnit.HOURS));
            for (Iterator iterator = storyTasks.iterator(); iterator.hasNext();) {
                com.technoetic.xplanner.domain.Task task = (com.technoetic.xplanner.domain.Task) iterator.next();
                Task taskLevelTask = storyLevelTask.addTask();
                taskLevelTask.setName(task.getName());
                taskLevelTask.setDuration(new MPXDuration(task.getEstimatedHours(), TimeUnit.HOURS));
                if (StringUtils.isNotEmpty(task.getDescription())) {
                    taskLevelTask.setNotes(filterString(task.getDescription()));
                }
                if (task.getAcceptorId() != 0) {
                    taskLevelTask.addResourceAssignment(resources.getResource(task.getAcceptorId()));
                } else {
                    taskLevelTask.setWork(new MPXDuration(task.getEstimatedHours(), TimeUnit.HOURS));
                }
                if (task.getActualHours() != 0) {
                    taskLevelTask.setActualWork(new MPXDuration(task.getActualHours(), TimeUnit.HOURS));
                    Date startTime = getStartTime(iterationLevelTask, task);
                    if (startTime != null) {
                        taskLevelTask.setActualStart(startTime);
                    }
                }
                if (task.getTimeEntries().size() > 0) {
                    earliestTaskStartTime = exportTimeEntries(task, taskLevelTask, earliestTaskStartTime);
                } else {
                    taskLevelTask.setActualStart(iterationLevelTask.getActualStart());
                }
            }
            if (earliestTaskStartTime == Long.MAX_VALUE) {
                earliestTaskStartTime = iterationLevelTask.getStart().getTime();
            }
            List tasks = storyLevelTask.getChildTasks();
            for (int i = 0; i < tasks.size(); i++) {
                Task task = (Task) tasks.get(i);
                if (task.getActualStart() == null) {
                    task.setActualStart(new Date(earliestTaskStartTime));
                }
            }
        } else {
            storyLevelTask.setActualStart(iterationLevelTask.getStart());
            storyLevelTask.setDuration(new MPXDuration(userStory.getEstimatedHours(), TimeUnit.HOURS));
            storyLevelTask.setWork(new MPXDuration(userStory.getEstimatedHours(), TimeUnit.HOURS));
            int trackerId = userStory.getTrackerId();
            if (trackerId != 0) {
                storyLevelTask.addResourceAssignment(resources.getResource(trackerId));
            }
        }
    }

    private long exportTimeEntries(com.technoetic.xplanner.domain.Task task,
                                   Task taskLevelTask,
                                   long earliestTaskStartTime) {
        long startTime = Long.MAX_VALUE;
        for (Iterator timeItr = task.getTimeEntries().iterator(); timeItr.hasNext();) {
            TimeEntry timeEntry = (TimeEntry) timeItr.next();
            if (timeEntry.getStartTime() != null && timeEntry.getStartTime().getTime() < startTime) {
                startTime = timeEntry.getStartTime().getTime();
            }
        }
        if (startTime < Long.MAX_VALUE) {
            taskLevelTask.setActualStart(new Date(startTime));
            if (startTime < earliestTaskStartTime) {
                earliestTaskStartTime = startTime;
            }
        }
        return earliestTaskStartTime;
    }

    private Date getStartTime(Task iterationLevelTask, com.technoetic.xplanner.domain.Task task) {
        Date start = null;
        for (Iterator iterator = task.getTimeEntries().iterator(); iterator.hasNext();) {
            TimeEntry timeEntry = ((TimeEntry) iterator.next());
            Date timeEntryStart = timeEntry.getStartTime();
            if (timeEntryStart == null) {
                timeEntryStart = timeEntry.getReportDate();
                long durationMs = (long) timeEntry.getDuration() * 3600000L;
                long timeEntryEnd = timeEntryStart.getTime() + durationMs;
                if (timeEntryEnd > iterationLevelTask.getFinish().getTime()) {
                    timeEntryStart = new Date(iterationLevelTask.getFinish().getTime() - durationMs);
                }
            }
            if (start == null || (timeEntryStart != null && timeEntryStart.getTime() < start.getTime())) {
                start = timeEntryStart;
            }
        }
        return start;
    }


    protected static class ResourceRegistry {
        private HashMap resources = new HashMap();

        public ResourceRegistry(List people, MPXFile mpxFile) throws MPXException {
            try {
                for (int i = 0; i < people.size(); i++) {
                    Person person = (Person) people.get(i);
                    Resource resource = mpxFile.addResource();
                    resource.setName(person.getName());
                    resources.put(new Integer(person.getId()), resource);
                }
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception e) {
                throw new MPXException("error loading people for MPX export", e);
            }
        }

        public Resource getResource(int i) {
            return (Resource) resources.get(new Integer(i));
        }
    }
}
