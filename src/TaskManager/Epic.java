package TaskManager;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    private ArrayList<SubTask> subtuskList = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
       super(id, name, description, status);
   }

    public Epic (String name, String description){
        super(name, description);
    }

    public ArrayList<Integer> getSubtuskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void setSubtuskList(ArrayList<SubTask> subtuskList) {
        this.subtuskList = subtuskList;
    }

    public ArrayList<SubTask> getSubtaskList() {
        return subtuskList;
    }

    public void clearSubtusks() {
        subtaskIds.clear();
        subtuskList.clear();
    }

    public void addSubtuskList(SubTask subTask){
        subtuskList.add(subTask);
    }

    public ArrayList<SubTask> getSubtuskList(){
        return subtuskList;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtuskIds=" + subtaskIds +
                '}';
    }
}
