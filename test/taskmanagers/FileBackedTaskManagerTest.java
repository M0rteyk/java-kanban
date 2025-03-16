package taskmanagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import task.Epic;
import task.SubTask;
import task.Task;
import expection.ManagerSaveException;
import task.TaskStatus;

public class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {
        file = Files.createTempFile("task_manager_test", ".csv").toFile();
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void testSaveFile_createsFile() {
        manager.saveFile();


        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testLoadFromFile_loadsTasks() {

        Task task = new Task("test", "Task 1", TaskStatus.NEW);
        Epic epic = new Epic("test", "Epic 1", TaskStatus.NEW);
        SubTask subTask = new SubTask("test", "subTask 1", TaskStatus.NEW, 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subTask);


        manager.saveFile();


        FileBackedTaskManager newManager = new FileBackedTaskManager(file);
        newManager.loadFromFile(file);


        Task loadedTask = newManager.findTaskById(1);
        Epic loadedEpic = newManager.findEpicById(2);
        SubTask loadedSubTask = newManager.findSubtaskByID(3);

        assertNotNull(loadedTask);
        assertEquals("Task 1", loadedTask.getName());

        assertNotNull(loadedEpic);
        assertEquals("Epic 1", loadedEpic.getName());

        assertNotNull(loadedSubTask);
        assertEquals("subTask 1", loadedSubTask.getName());
    }

    @Test
    public void testSaveFile_correctlySavesHistory() {
        Task task = new Task("test", "Task 1", TaskStatus.NEW);
        manager.addTask(task);
        manager.saveFile();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("1,")) {
                    assertTrue(line.contains("1"));
                }
            }
        } catch (IOException e) {
            fail("Не удалось прочитать файл.");
        }
    }

    @Test
    public void testCreateTask_savesToFile() {
        Task task = new Task("test", "Task 1", TaskStatus.NEW);
        manager.addTask(task);


        manager.saveFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean taskSaved = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Task 1")) {
                    taskSaved = true;
                    break;
                }
            }
            assertTrue(taskSaved);
        } catch (IOException e) {
            fail("Не удалось прочитать файл.");
        }
    }

    @Test
    public void testDeleteTask_deletesFromFile() {
        Task task = new Task("test", "Task 1", TaskStatus.NEW);
        manager.addTask(task);
        manager.saveFile();

        manager.deleteTaskById(1);
        manager.saveFile();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean taskFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Task 1")) {
                    taskFound = true;
                    break;
                }
            }
            assertFalse(taskFound);
        } catch (IOException e) {
            fail("Не удалось прочитать файл.");
        }
    }

    @Test
    public void testManagerSaveException_isThrownIfFileNotWritable() {

        File readOnlyFile = new File("/path/to/a/non/writable/file.csv");

        FileBackedTaskManager managerWithBadFile = new FileBackedTaskManager(readOnlyFile);

        assertThrows(ManagerSaveException.class, () -> {
            managerWithBadFile.saveFile();
        });
    }

    @Test
    public void testHistoryToString_convertsHistoryCorrectly() {
        Task task1 = new Task("test", "Task 1", TaskStatus.NEW);
        Task task2 = new Task("test", "Task 2", TaskStatus.NEW);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.findTaskById(1);
        manager.findTaskById(2);

        manager.saveFile();

        String historyString = FileBackedTaskManager.historyToString(manager.getHistoryManager());
        assertTrue(historyString.contains("1"));
        assertTrue(historyString.contains("2"));
    }

    @Test
    public void testHistoryFromString_parsesHistoryCorrectly() {
        String historyString = "1,2";
        List<Integer> history = FileBackedTaskManager.historyFromString(historyString);

        assertEquals(2, history.size());
        assertTrue(history.contains(1));
        assertTrue(history.contains(2));
    }

    @Test
    public void testLoadFromFile_whenFileIsEmpty_doesNotThrowError() {

        file.delete();
        manager.saveFile();
        manager.loadFromFile(file);
    }


}
