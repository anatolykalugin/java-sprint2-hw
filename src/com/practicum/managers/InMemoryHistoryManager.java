package com.practicum.managers;

import com.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;
    Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        Node nodeToPut = new Node(task);
        if (nodeMap.get(task.getId()) != null) {
            removeNode(nodeMap.get(task.getId()));
        }
        linkLast(nodeToPut);
        nodeMap.put(task.getId(), nodeToPut);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public boolean containsTask(int id) {
        return nodeMap.containsKey(id);
    }

    private void linkLast(Node node) {
        Node oldTail = tail;
        tail = node;
        if (head == null) {
            head = node;
            head.setNext(tail);
            tail.setPrev(head);
            head.setPrev(null);
        } else {
            tail.setPrev(oldTail);
            tail.getPrev().setNext(tail);
        }
        tail.setNext(null);
    }

    public void removeNode(Node node) {
        if ((node == head) && (head == tail)) {
            head = head.getNext();
            tail = tail.getPrev();
        } else if (node == head) {
            node.getNext().setPrev(null);
            head = node.getNext();
            node.setNext(null);
        } else if ((node.getPrev() != null) && (node.getNext() != null)) {
            node.getNext().setPrev(node.getPrev());
            node.getPrev().setNext(node.getNext());
        } else if ((node.getPrev() == null) && (node.getNext() == null)) {
            head = null;
            tail = null;
        }  else {
            if (node.getPrev() != null) {
                node.getPrev().setNext(null);
                tail = node.getPrev();
                node.setPrev(null);
            } else {
                node.setPrev(tail);
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> searchList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            searchList.add(node.data);
            if ((node.getNext() == null) || (node.data.equals(node.getNext().data))) {
                break;
            }
            node = node.getNext();
        }
        return searchList;
    }

}

class Node {

    public Task data;
    private Node next;
    private Node prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return this.prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Просмотр задачи: " + data;
    }

}
