import taskmanagers.Managers;
import taskmanagers.TaskManager;
import task.Task;
import task.Epic;
import task.SubTask;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Path path = Path.of("src/resourses/data.csv");
        File file = path.toFile();
        TaskManager manager = Managers.getDefaultTaskManager(file);
        printAllTasks(manager);
        openFile(file);
    }

    private static void createTasks(TaskManager manager) {
        LocalDateTime now = LocalDateTime.now();

        Task writeCode = new Task("Написать программу", "На JAVA", now.plusHours(4), Duration.ofMinutes(30));
        manager.createTask(writeCode);


        Task review = new Task("Отправить на ревью", "Выгрузить код на GitHub", now.plusHours(7), Duration.ofMinutes(30));
        manager.createTask(review);


        Epic codeStructure = new Epic("Определить структуру кода", "Понять задачи");
        manager.createEpic(codeStructure);


        SubTask mainTusk = new SubTask("Выделить основные задачи", "Прочитать ТЗ несколько раз", now.plusHours(5), Duration.ofMinutes(30), codeStructure.getId());
        manager.createSubtusk(mainTusk);


        SubTask createClass = new SubTask("Создать классы", "Создать классы опираясь на ТЗ", now.plusHours(3), Duration.ofMinutes(26), codeStructure.getId());
        manager.createSubtusk(createClass);


        Epic continueCode = new Epic("Наполнить код", "Дописать все необходимое для работы программы");
        manager.createEpic(continueCode);


        SubTask createMetods = new SubTask("Написать конструкторы и методы", "Опираясь на ТЗ написать конструкторы и методы для корректной работы программы", now.plusHours(8), Duration.ofMinutes(26), continueCode.getId());
        manager.createSubtusk(createMetods);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Задачи по приоритету времени:");
        for (Task task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        System.out.println("Задачи которые пересекаются:");
        for (String conflict : manager.findTimeConflicts()) {
            System.out.println(conflict);
        }

    }

    private static void openFile(File file) {
        if (file.exists()) {
            try {
                // Используем Desktop для открытия файла
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file); // Открытие файла с помощью ассоциированного приложения
            } catch (IOException e) {
                System.out.println("Ошибка при открытии файла: " + e.getMessage());
            }
        } else {
            System.out.println("Файл не найден: " + file.getAbsolutePath());
        }
    }


}
