package taskmanagers;

import expection.ManagerSaveException;
import file.TaskCSVFormatHeader;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод сохранения данных в файл
    public void saveFile() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(TaskCSVFormatHeader.getHeader());


            for (Task task : getAllTasks()) {
                writer.write(TaskCSVFormatHeader.toString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                writer.write(TaskCSVFormatHeader.toString(epic) + "\n");
            }

            for (SubTask subtask : getSubTasks()) {
                writer.write(TaskCSVFormatHeader.toString(subtask) + "\n");
            }

            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    // метод загрузки данных из файла при запуске программы
    public FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager result = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.equals("")) {
                    break;
                }

                Task task = TaskCSVFormatHeader.fromString(line);

                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof SubTask subtask) {
                    addSubtask(subtask);
                } else {
                    addTask(task);
                }
            }

            String lineWithHistory = bufferedReader.readLine();
            for (int id : historyFromString(lineWithHistory)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }

        return result;
    }

    // Метод для сохранения истории в CSV
    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder str = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            str.append(task.getId()).append(",");
        }

        if (str.length() != 0) {
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();
    }

    // Метод восстановления менеджера истории из CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> toReturn = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");

            for (String number : id) {
                toReturn.add(Integer.parseInt(number));
            }

            return toReturn;
        }
        return toReturn;
    }

    public Task addTask(Task task) {
        return super.createTask(task);

    }

    public Epic addEpic(Epic epic) {

        return super.createEpic(epic);
    }

    public SubTask addSubtask(SubTask subtask) {

        return super.createSubtusk(subtask);
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

}


