package TaskManagers;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class InMemoryHistoryTaskManager implements HistoryManager {

    private static class CutomList {

        private final HashMap<Integer, Node> nodeMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node node = new Node();
            node.setTask(task);

            if (nodeMap.containsKey(task.getId())) {
                removeNode(nodeMap.get(task.getId()));
            }

            if (head == null) {
                tail = node;
                head = node;
                node.setNext(null);
                node.setPrev(null);
            } else {
                node.setPrev(tail);
                node.setNext(null);
                tail.setNext(node);
                tail = node;
            }

            nodeMap.put(task.getId(), node);
        }

        private List<Task> getTasks() {
            List<Task> result = new ArrayList<>();
            Node node = head;
            while (Objects.nonNull(node)) {
                result.add(node.getTask());
                node = node.next;
            }
            return result;
        }


        private void removeNode(Node node) {
            if (node != null) {
                nodeMap.remove(node.getTask().getId());
                Node prev = node.getPrev();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrev();
                }

                if (prev != null) {
                    prev.setNext(next);
                }

                if (next != null) {
                    next.setPrev(prev);
                }
            }

        }

        private Node getNode(int id) {
            return nodeMap.get(id);
        }
    }

    CutomList cutomList = new CutomList();

    @Override
    public void addTask(Task task) {
        cutomList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        cutomList.removeNode(cutomList.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return cutomList.getTasks();

    }


    public static class Node {

        Task task;
        Node next;
        Node prev;

        public Task getTask() {
            return task;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
