package ChaZhao;

import java.util.ArrayList;

public interface UserService {

    int findIndexById(ArrayList<User> list, int id) throws UserException;

    void updateMaxAgeUserId(ArrayList<User> list, int newId) throws UserException;

    void printMalesYoungerThanAvg(ArrayList<User> list) throws UserException;

    void sortByAgeDesc(ArrayList<User> list) throws UserException;

    ArrayList<User> splitBySex(ArrayList<User> list) throws UserException;

    void insertAtHead(ArrayList<User> list, User user) throws UserException;

    void printList(ArrayList<User> list, String title) throws UserException;
}
