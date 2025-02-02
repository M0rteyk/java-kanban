package task;

public class SubTask extends Task {

    private final int epicId;


    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }



    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}
