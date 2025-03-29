package taskmanagers;

import expection.ManagerSaveException;
import file.TaskCSVFormatHeader;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод сохранения данных в файл
    public void saveFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(TaskCSVFormatHeader.getHeader());

            getAllTasks().stream()
                    .map(TaskCSVFormatHeader::toString)
                    .forEach(str -> writeToFile(writer, str));

            getAllEpics().stream()
                    .map(TaskCSVFormatHeader::toString)
                    .forEach(str -> writeToFile(writer, str));

            getSubTasks().stream()
                    .map(TaskCSVFormatHeader::toString)
                    .forEach(str -> writeToFile(writer, str));

            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    // метод загрузки данных из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager result = new FileBackedTaskManager(file);
        int maxId = 0; // Переменная для восстановления последнего ID

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();

            // Чтение задач
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                Task task = TaskCSVFormatHeader.fromString(line);
                maxId = Math.max(maxId, task.getId());

                if (task instanceof Epic epic) {
                    result.createEpic(epic);
                } else if (task instanceof SubTask subtask) {
                    result.createSubtusk(subtask);
                } else {
                    result.createTask(task);
                }
            }

            // Чтение истории
            String lineWithHistory = bufferedReader.readLine();
            if (lineWithHistory != null && !lineWithHistory.isEmpty()) {
                historyFromString(lineWithHistory).forEach(result::addToHistory);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }

        result.genId = maxId;
        return result;
    }

    @Override
    public Task createTask(Task task) {

        Task innerTask = super.createTask(task);
        saveFile();
        return innerTask;
    }

    @Override
    public Task updateTask(Task task) {

        Task innerTask = super.updateTask(task);
        saveFile();
        return innerTask;
    }

    @Override
    public Task findTaskById(int id) {
        Task innerTask = super.findTaskById(id);

        saveFile();

        return innerTask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveFile();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveFile();
    }

    @Override
    public Epic createEpic(Epic epic) {

        Epic innerEpic = super.createEpic(epic);
        saveFile();
        return innerEpic;
    }

    @Override
    public Epic updateEpic(Epic epic) {

        Epic innerEpic = super.updateEpic(epic);
        saveFile();
        return innerEpic;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic innerEpic = super.findEpicById(id);
        saveFile();
        return innerEpic;
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        saveFile();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        saveFile();
    }

    @Override
    public SubTask createSubtusk(SubTask subTask) {

        SubTask innerSubtask = super.createSubtusk(subTask);
        saveFile();
        return innerSubtask;
    }

    @Override
    public SubTask updateSubtask(SubTask subTask) {

        SubTask innerSubtask = super.updateSubtask(subTask);
        saveFile();
        return innerSubtask;
    }

    @Override
    public SubTask findSubtaskByID(int id) {
        SubTask innerSubtask = super.findSubtaskByID(id);
        saveFile();
        return innerSubtask;
    }

    @Override
    public void clearSubtusks() {
        super.clearSubtusks();
        saveFile();
    }

    @Override
    public void clearSubtusksById(int id) {
        super.clearSubtusksById(id);
        saveFile();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public List<String> findTimeConflicts() {
        return super.findTimeConflicts();
    }

    // Метод для сохранения истории в CSV
    private static String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    // Метод восстановления менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    // Вспомогательный метод для записи в файл
    private void writeToFile(BufferedWriter writer, String str) {
        try {
            writer.write(str + "\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл", e);
        }
    }

}


