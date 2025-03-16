import taskmanagers.Managers;
import taskmanagers.TaskManager;
import taskmanagers.FileBackedTaskManager;
import task.Task;
import task.Epic;
import task.SubTask;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Path path = Path.of("data.csv");
        File file = new File(String.valueOf(path));
        TaskManager manager = Managers.getDefaultTaskManager(file);
        createTasks(manager);
        //System.out.println(manager.findTaskById(1));
        // manager.findTaskById(2);
        // manager.findEpicById(3);
        printAllTasks(manager);
        openFile(file);


        //System.out.println(manager.getAllEpics());
        // manager.findTaskById(1);
        // manager.findTaskById(2);
        // manager.findEpicById(3);
        // manager.findTaskById(1);
        // manager.findEpicById(3);
        //System.out.println(manager.getHistory());
        // FileBackedTaskManager ff = new FileBackedTaskManager(file);
        ////  ff.loadFromFile(file);
        // System.out.println(ff.getAllEpics());


    }

    private static void createTasks(TaskManager manager) {
        Task writeCode = new Task("Написать программу", "На JAVA");
        manager.createTask(writeCode);


        Task review = new Task("Отправить на ревью", "Выгрузить код на GitHub");
        manager.createTask(review);


        Epic codeStructure = new Epic("Определить структуру кода", "Понять задачи");
        manager.createEpic(codeStructure);


        SubTask mainTusk = new SubTask("Выделить основные задачи", "Прочитать ТЗ несколько раз", codeStructure.getId());
        manager.createSubtusk(mainTusk);


        SubTask createClass = new SubTask("Создать классы", "Создать классы опираясь на ТЗ", codeStructure.getId());
        manager.createSubtusk(createClass);


        Epic continueCode = new Epic("Наполнить код", "Дописать все необходимое для работы программы");
        manager.createEpic(continueCode);


        SubTask createMetods = new SubTask("Написать конструкторы и методы", "Опираясь на ТЗ написать конструкторы и методы для корректной работы программы", continueCode.getId());
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
