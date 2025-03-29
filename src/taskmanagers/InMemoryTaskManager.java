package taskmanagers;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );

    int genId = 0;

    /*
     *Работа с задачами(Task)
     * Методы создания, обновления, поиска и тд.
     */

    @Override
    public Task createTask(Task task) {

        int newId = generateId();
        if (task.getId() == 0) {
            task.setId(newId);
        }
        tasks.put(task.getId(), task);
        addToPriority(task); // Добавляем в отсортированный набор
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return task;
        }

        removeFromPriority(tasks.get(task.getId()));
        tasks.replace(task.getId(), task);
        addToPriority(task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task findTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задачи с таким ID нет");
            return;
        }
        removeFromPriority(tasks.get(id));
        tasks.remove(id);
    }

    /*
     *Работа с эпиками(Epic)
     * Методы создания, обновления, поиска и тд.
     */

    @Override
    public Epic createEpic(Epic epic) {
        if (epic.getId() == 0) {
            epic.setId(generateId());
        }
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {

        if (!epics.containsKey(epic.getId())) {
            return epic;
        }

        epics.replace(epic.getId(), epic);

        updateEpicStatus(epic);
        return epic;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public void deleteAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким ID нет");
            return;
        }
        epics.get(id).getSubtuskIds().forEach(subtasks::remove);
        epics.remove(id);
    }

    /*
     *Работа с подзадачами(SubTask)
     * Методы создания, обновления, поиска и тд.
     */

    @Override
    public SubTask createSubtusk(SubTask subTask) {

        if (subTask.getEpicId() == 0 || !epics.containsKey(subTask.getEpicId())) {
            return subTask;
        }

        int newId = (subTask.getId() == 0) ? generateId() : subTask.getId();
        subTask.setId(newId);
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubtuskIds().add(newId);
        updateEpicStatus(epic);
        updateEpicTimes(epic);
        addToPriority(subTask);
        return subTask;
    }

    @Override
    public SubTask updateSubtask(SubTask subTask) {

        if (subTask.getEpicId() == 0 || !epics.containsKey(subTask.getEpicId())) {
            return subTask;
        }

        removeFromPriority(subTask);
        subtasks.replace(subTask.getId(), subTask);
        addToPriority(subTask);

        Epic epic = epics.get(subTask.getEpicId());
        updateEpicTimes(epic);
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
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(Epic epic) {
        return epic.getSubtuskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCountEpicSubtusk(Epic epic) {
        return epic.getSubtuskIds();
    }

    @Override
    public void clearSubtusks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtusks();
            epic.setStatus(TaskStatus.NEW);
        }

    }

    @Override
    public void clearSubtusksById(int id) {
        Optional.ofNullable(subtasks.get(id))
                .ifPresentOrElse(
                        subTask -> {
                            removeFromPriority(subTask);
                            subtasks.remove(id);
                            epics.get(subTask.getEpicId()).getSubtuskIds().remove(Integer.valueOf(id));
                        },
                        () -> System.out.println("Подзадачи с таким ID нет")
                );
    }


    /*
     *Вспомогательные методы
     * Получение истории просмотров, обновление эпиков и генерация ID
     */

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Сортируем задачи по приоритетности времени
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Находим временные пересечения в задачах
    @Override
    public List<String> findTimeConflicts() {
        List<Task> tasks = getPrioritizedTasks();

        return IntStream.range(0, tasks.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, tasks.size())
                        .filter(j -> hasTimeOverlap(tasks.get(i), tasks.get(j)))
                        .mapToObj(j -> String.format("Конфликт: %s (ID %d) и %s (ID %d)",
                                tasks.get(i).getName(), tasks.get(i).getId(),
                                tasks.get(j).getName(), tasks.get(j).getId()))
                )
                .collect(Collectors.toList());
    }

    private void addToPriority(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeFromPriority(Task task) {
        prioritizedTasks.remove(task);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void addToHistory(int id) {
        Optional.<Task>ofNullable(tasks.get(id))
                .or(() -> Optional.ofNullable(subtasks.get(id)))
                .or(() -> Optional.ofNullable(epics.get(id)))
                .ifPresent(historyManager::addTask);
    }

    private void updateEpicStatus(Epic epic) {
        Map<TaskStatus, Long> statusCount = epic.getSubtuskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        SubTask::getStatus,
                        Collectors.counting()
                ));

        long totalSubtasks = epic.getSubtuskIds().size();

        if (statusCount.getOrDefault(TaskStatus.NEW, 0L) == totalSubtasks) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusCount.getOrDefault(TaskStatus.DONE, 0L) == totalSubtasks) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int generateId() {
        return ++genId;
    }

    public boolean hasTimeOverlap(Task task1, Task task2) {
        if (task1 == null || task2 == null
                || task1.getStartTime() == null || task1.getEndTime() == null
                || task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return !task1.getEndTime().isBefore(task2.getStartTime())
                && !task1.getStartTime().isAfter(task2.getEndTime());
    }

    // Метод обновления времени эпиков по подзадачам
    private void updateEpicTimes(Epic epic) {
        if (epic == null) {
            return;
        }
        List<SubTask> subtasks = getEpicSubtasks(epic);
        epic.updateTimes(subtasks);
    }

}
