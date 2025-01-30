package TaskManagers;

public class Managers {

    private Managers(){

    }

    public static TaskManager getDefaultTaskManager(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryTaskManager();
    }
}
