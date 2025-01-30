package TaskManagers;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    private int genId = 0;

    ///////////////////////РАБОТА С TASK//////////////////////////

    @Override
    public Task createTask(Task task){

        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
        return task;

    }

    @Override
    public Task updateTask(Task task){

        if (!tasks.containsKey(task.getId())){
            return task;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task findTaskById(int id){
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }

    @Override
    public Object deleteTaskById(int id){
        tasks.remove(id);
        return null;
    }

    //////////////////////РАБОТА С EPIC//////////////////////

    @Override
    public Epic createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic){

        if (!epics.containsKey(epic.getId())){
            return epic;
        }

        epics.replace(epic.getId(), epic);

        updateEpicStatus(epic);
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public void deleteAllEpic () {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Object deleteEpicById (int id) {
        ArrayList<Integer> ids = epics.get(id).getSubtuskIds();
        epics.remove(id);
        for (Integer subid: ids) {
            subtasks.remove(subid);
        }
        return null;
    }

    ///////////////////////РАБОТА С SUBTASK///////////////////////

    @Override
    public SubTask createSubtusk(SubTask subTask) {

        if (subTask.getEpicId() == 0){
            return subTask;
        }

        if (!epics.containsKey(subTask.getEpicId())){
            return subTask;
        }

        int newId = generateId();
        subTask.setId(newId);
        subtasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubtuskIds().add(newId);
        updateEpicStatus(epic);
        return subTask;
    }

    @Override
    public SubTask updateSubtask(SubTask subTask){

        if (subTask.getEpicId() == 0){
            return subTask;
        }

        if (!epics.containsKey(subTask.getEpicId())){
            return subTask;
        }

        SubTask oldSub = subtasks.get(subTask.getId());
        subtasks.replace(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatus(epic);
        return subTask;
    }

    @Override
    public SubTask findSubtaskByID(int id) {
        SubTask subTask = subtasks.get(id);
        historyManager.addTask(subTask);
        return subTask;
    }

    @Override
    public ArrayList<SubTask> getSubTasks () {
         return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(Epic epic){
         ArrayList<Integer> ids = epic.getSubtuskIds();
         ArrayList<SubTask> subs = new ArrayList<>();
         for (Integer id : ids){
             subs.add(subtasks.get(id));
         }
        return subs;

    }

    @Override
    public ArrayList<Integer> getCountEpicSubtusk(Epic epic){
        return epic.getSubtuskIds();
    }

    @Override
    public void clearSubtusks(){
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.clearSubtusks();
            epic.setStatus(TaskStatus.NEW);
        }

    }

    @Override
    public Object clearSubtusksById (int id){
        SubTask subTask = subtasks.get(id);
        int epicID = subTask.getEpicId();
        subtasks.remove(id);

        Epic epic = epics.get(epicID);
        ArrayList<Integer> subtuskIds = epic.getSubtuskIds();
        subtuskIds.remove(Integer.valueOf(id));
        epic.setSubtaskIds(subtuskIds);
        updateEpic(epic);
        return null;
    }


    ////////////////////////////ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ//////////////////////////////

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic){
        int doneStatusCount = 0;
        int newStatusCount = 0;
        ArrayList<Integer> subtask = epic.getSubtuskIds();
        ArrayList<SubTask> subb = new ArrayList<>();

        for (Integer id : subtask) {
            subb.add(subtasks.get(id));
        }

        for (SubTask subTask : subb){
            if (subTask.getStatus() == TaskStatus.NEW){
                newStatusCount++;
            }
            if (subTask.getStatus() == TaskStatus.DONE){
                doneStatusCount++;
            }
        }

        if(newStatusCount == subtask.size()){
            epic.setStatus(TaskStatus.NEW);
        } else if (doneStatusCount == subtask.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int generateId(){
        return ++genId;
    }
}
